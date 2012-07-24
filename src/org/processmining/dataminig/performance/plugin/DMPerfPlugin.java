package org.processmining.dataminig.performance.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeLiteral;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.model.impl.XAttributeBooleanImpl;
import org.deckfour.xes.model.impl.XAttributeContinuousImpl;
import org.deckfour.xes.model.impl.XAttributeDiscreteImpl;
import org.deckfour.xes.model.impl.XAttributeLiteralImpl;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.models.graphbased.directed.petrinet.elements.Place;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.plugins.petrinet.replay.conformance.TotalConformanceResult;
import org.processmining.plugins.petrinet.replay.performance.PerformanceData;
import org.processmining.plugins.petrinet.replay.performance.PerformanceResult;
import org.processmining.plugins.petrinet.replay.performance.TotalPerformanceResult;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

public class DMPerfPlugin {
	@Plugin(name = "Log to arff", 
	        parameterLabels = { "Log", "PerformanceResult" }, 
	        returnLabels = { "Weka Instance" }, 
	        returnTypes = { Instances.class }  )
	@UITopiaVariant(
			uiLabel="Weka Performance Instances",
			affiliation = "University of Pisa",
			author = "Hind",
			email = "hind")
			
	public Object generateWekaInstances(PluginContext context, XLog log, TotalPerformanceResult performance) throws Exception{
		
		//attributi che per la conformance non servono
		List<String> NonInteressanti = new Vector<String>();
	    NonInteressanti.add("org:resource");
	    NonInteressanti.add("time:timestamp");
	    NonInteressanti.add("concept:name");
	    NonInteressanti.add("lifecycle:transition");
	    
		Map<String,Set<String>> litAttributesValues = new HashMap<String,Set<String>>();
		Set<String> litAttributes = new HashSet<String>();
		Set<String> floatAttributes = new HashSet<String>();
		Set<String> boolAttributes = new HashSet<String>();
		
				
		/*Ricavo tutti gli attributi con una scansione*/
		for( XTrace t : log ){
			Map<String,Integer> CountAttributes = new HashMap<String,Integer>();
			for( XEvent e : t ){				
				  XAttributeMap attrs = e.getAttributes();
				  for(String key: attrs.keySet()){
					  if( CountAttributes.containsKey(key)){
						  CountAttributes.put(key, new Integer(CountAttributes.get(key).intValue() + 1));
					  }
					  else{
						  
						  CountAttributes.put(key, new Integer(0));
					  }
						  
					  if (! NonInteressanti.contains(key))
					  {
						  XAttribute val = attrs.get(key);
						  if( val instanceof XAttributeContinuousImpl || 
								  val instanceof XAttributeDiscreteImpl )
							  floatAttributes.add(key + "^" + CountAttributes.get(key).toString());
						  if( val instanceof XAttributeBooleanImpl)
							  boolAttributes.add(key + "^" + CountAttributes.get(key).toString());
						  if( val instanceof XAttributeLiteralImpl ){
							  litAttributes.add(key + "^" + CountAttributes.get(key).toString());
							  if(! litAttributesValues.containsKey(key))
								  litAttributesValues.put(key, new HashSet<String>());
							  litAttributesValues.get(key).add(((XAttributeLiteral) val).getValue());
						  }
					  }
				  }				 				  
			}
		}
		
		/**Header**/
		int capacity = log.size();
		String name_log = ((XAttributeLiteralImpl) log.getAttributes().get("concept:name")).getValue();
		if( name_log == null )
			name_log = "out";

		/**Attributes**/
		ArrayList<Attribute> wekaAttrList = new ArrayList<Attribute>();
		
		/*file arff*/
    	 List<String> litAttrNames = new Vector<String>();
		 litAttrNames.addAll(litAttributes);
		 //Gli attributi Lit
		 for( String atr : litAttrNames){
			 String key = atr.substring(0, atr.indexOf("^"));
			 Set<String> values = litAttributesValues.get(key);
			 Attribute wekaAtr = new Attribute(atr, new Vector<String>(values));
			 wekaAttrList.add(wekaAtr);
		 }
		  
		 //Gli attributi float
		 Vector<String> floatAttrNames = new Vector<String>();
		 floatAttrNames.addAll(floatAttributes);
		  for( String atr: floatAttrNames){
			  Attribute wekaAtr = new Attribute(atr);
			  wekaAttrList.add(wekaAtr);
		  }
		  
		  //Gli attributi bool
		  Vector<String> boolAttrNames = new Vector<String>();
		  boolAttrNames.addAll(boolAttributes);
		  for( String atr: boolAttrNames){
			  Vector<String> values = new Vector<String>();
			  values.add("TRUE");
			  values.add("FALSE");
			  Attribute wekaAttr = new Attribute(atr, values);
			  wekaAttrList.add(wekaAttr);
		  }
		    
		  Vector<String> values = new Vector<String>();
		  values.add("HIGH");
		  values.add("LOW");
		  values.add("OK");
		  values.add("?");
		  Attribute wekaAttr = new Attribute("Sync", values);
		  wekaAttrList.add(wekaAttr);

		  
		  Instances instances = new Instances(name_log, wekaAttrList, capacity);
		
		
     	/**Data**/
		  
		  for(XTrace tr: log ){
			  DenseInstance instance = new DenseInstance(wekaAttrList.size());
			  instance.setDataset(instances);
			  
			  Map<String,String> trAttributes = new HashMap<String,String>();
			  Map<String,Integer> CountAttributes = new HashMap<String,Integer>();

			  for(XEvent ev : tr){
				  if(((XAttributeLiteral) ev.getAttributes().get("lifecycle:transition")).getValue().equals("complete"))
					  continue;
				  XAttributeMap attrs = ev.getAttributes();
				  for( String key: attrs.keySet()){
					  if( CountAttributes.containsKey(key)){
						  int count = CountAttributes.get(key).intValue();
						  CountAttributes.put(key, new Integer(count + 1));
					  }
					  else{
						  
						  CountAttributes.put(key, new Integer(0));
					  }
					  if( NonInteressanti.contains(key))
						  continue;
					  XAttribute val = attrs.get(key);
					  if( val instanceof XAttributeContinuousImpl){
						   trAttributes.put(key  + "^" + CountAttributes.get(key).toString() ,Double.toString(((XAttributeContinuousImpl) val).getValue()));
					  }
					  if(val instanceof XAttributeDiscreteImpl){
						  trAttributes.put(key  + "^" + CountAttributes.get(key).toString(), Long.toString(((XAttributeDiscreteImpl) val).getValue()));
					  }				  
					  if( val instanceof XAttributeBooleanImpl){
						  trAttributes.put(key  + "^" + CountAttributes.get(key).toString(),Boolean.toString(((XAttributeBooleanImpl) val).getValue()));
					  }
					  if( val instanceof XAttributeLiteralImpl ){
						  trAttributes.put(key  + "^" + CountAttributes.get(key).toString(), ((XAttributeLiteral) val).getValue());
					  }
						  
					  
				  }
			  }
			  
			  int wekaAttrindex = 0;
			  
			  for(String key: litAttrNames){
				  String trvalue = trAttributes.get(key);
				  if(trvalue != null ) {
					  instance.setValue(wekaAttrindex, trvalue);
				  }
				  wekaAttrindex++;
			  }
			  for(String key: floatAttrNames){
				  String trvalue = trAttributes.get(key);
				  if(trvalue != null ) {
					  instance.setValue(wekaAttrindex, Float.valueOf(trvalue));
				  }
				  wekaAttrindex++;
			  }
				  
			  	for(String key: boolAttrNames){
			  		String trvalue = trAttributes.get(key);
			  		if(trvalue != null ) {
						  instance.setValue(wekaAttrindex, trvalue);
			  		}
			  		wekaAttrindex++;	
			  	}
				  
				  //conformance
			  	/*Marking missing = performance.getList().get(log.indexOf(tr)).getMissingMarking();*

			  	if(missing.isEmpty() ) {
			  		instance.setValue(wekaAttrindex, "TRUE");
		  		}
			  	else {
			  		instance.setValue(wekaAttrindex, "FALSE");
			  	}	
		  		wekaAttrindex++;
		  		
		  		instances.add(instance);
		  }
		  */

			  	
			  	//performance
			 
			 List<PerformanceResult> Perf = performance.getListperformance();
			 System.out.println(Perf.size());
			 PerformanceResult trPerf= 	Perf.get(log.indexOf(tr));
			 Map<Place, PerformanceData> PlaceData = trPerf.getList();
			 List<Float> synchTimes = new Vector<Float>();
			 for( Place p : PlaceData.keySet() ){
				 float synch = PlaceData.get(p).getSynchTime();
				 if (synch != 0 )
				 	 synchTimes.add(new Float(synch));				 
				/* if( synch < 0 )
						 synchTimes.add( new Float(- synch));				  
			 	*/
			 }
			 float maxTime = 0;
			 for(Float time : synchTimes){
				 if (time.floatValue() > maxTime )
					 maxTime = time.floatValue();
			 }
			 
			 System.out.println("**********TOSRING********");
			 System.out.println(maxTime);
			 System.out.println("*************************");
			 
			 if(maxTime > 1000 ) 
				 instance.setValue(wekaAttrindex, "HIGH");
			 else
				 instance.setValue(wekaAttrindex, "LOW");
			 
			 wekaAttrindex++;
		  		
		  	instances.add(instance);
			  	/*System.out.println("**************TOSRING**********");
			  	performance.toString();
			  	System.out.println("************************");
*/
		  }
		 
		 return instances;		

	}
}
