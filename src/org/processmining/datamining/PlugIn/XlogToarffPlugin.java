package org.processmining.datamining.PlugIn;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.model.impl.XAttributeBooleanImpl;
import org.deckfour.xes.model.impl.XAttributeContinuousImpl;
import org.deckfour.xes.model.impl.XAttributeDiscreteImpl;
import org.deckfour.xes.model.impl.XAttributeLiteralImpl;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;


@Plugin(name = "XlogToarff", parameterLabels = { "Log" }, returnLabels = { "XlogToarffPlugIn" }, returnTypes = {String.class })


public class XlogToarffPlugin {
	
	 public String XlogArff(PluginContext context, XLog log ) throws IOException{
		 
		 Map<String, Vector<String>>  map = new HashMap<String, Vector<String>>();
		 Vector<String> istances = new Vector<String>();
		  
		  for( XTrace c: log ){
			  String str = "" ;
			  for( XEvent e : c){
				 XAttributeMap attrs = e.getAttributes();
				 for( String key: attrs.keySet() ){
					 // se è un attributo di quelli che mi interessano:
					 if( !key.equals("org:resource") && !key.equals("lifecycle:transition")
							 && !key.equals("concept:name") && !key.equals("time:timestamp") ){
						 
						 // se è un attributo nuovo lo aggiungo
						 if(!map.containsKey(key)){
							 
							 Vector<String> valori = new Vector<String>();
							 
							 // devo verificare di che tipo è l'attributo
							 // ha senso ??!
							 if( attrs.get(key) instanceof XAttributeContinuousImpl  || 
									 attrs.get(key) instanceof XAttributeDiscreteImpl )
								 valori.add("NUMEERIC");
							 else if(attrs.get(key) instanceof XAttributeBooleanImpl )
								 valori.add("BOOL");
							 
							 else if( attrs.get(key) instanceof XAttributeLiteralImpl){
								 valori.add("NOMINAL");
								 valori.add(attrs.get(key).toString());
							 }
							
							 map.put(key, valori);							 
						 }
						 
						 // se è un attributo che c'è già ed è di tipo NOMINAL aggiungo il valore se non c'è già
						 Vector<String> vec = map.get(key);
						 if( vec.elementAt(0).equals("NOMINAL")){
							 if( ! vec.contains(attrs.get(key).toString()))
								 	vec.add(attrs.get(key).toString()); 
						 }
						 
						 str += attrs.get(key).toString() + ",";
					 }
				 }
			  }
			  
			  istances.add(str.substring(0, str.length() - 2 )) ;
		  }
		  
		  // A questo punto devo mettere tutto in un file
		  
		  FileWriter fstream = new FileWriter("out.arff");
		  BufferedWriter result = new BufferedWriter(fstream);
		  String val = ((XAttributeLiteralImpl) log.getAttributes().get("concept:name")).getValue();
		  result.write("@relation" + val + "\n" );
		  result.write("\n");
		  		  
		  for( String chiave : map.keySet()){
			  Vector<String> valori = map.get(chiave);

			  if( valori.elementAt(0).equals("NUMERIC") )
				  result.write("@attribute " + chiave + " NUMERIC" + "\n");
			  if( valori.elementAt(0).equals("BOOL"))
				  result.write("@attribute " + chiave + " {TRUE, FALSE}" + "\n");
			  if( valori.elementAt(0).equals("NOMINAL")){
				  int i ;
				  result.write("@attribute " + chiave + "{");
				  for( i = 1; i<valori.capacity() - 1; i++ ){
					  result.write(valori.elementAt(i) + ", ");
				  }
				  
				  i += 1 ;
				  result.write(valori.elementAt(i) + "}\n");
			  }
			  
			result.write("\n");	  
		  }
		  
		  result.write("@data\n");
		  
		  for(int i=0; i<istances.capacity(); i++){
			  result.write(istances.elementAt(i) + "\n");
		  }
		  		  
		  result.flush();
		  			  
		return null;
	 }
}