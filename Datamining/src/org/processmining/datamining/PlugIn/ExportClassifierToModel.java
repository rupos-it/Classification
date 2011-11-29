package org.processmining.datamining.PlugIn;

import java.io.File;

import org.processmining.contexts.uitopia.annotations.UIExportPlugin;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;

import weka.classifiers.Classifier;

@Plugin(name = "Export Classifier to Model File", parameterLabels = { "Classifier", "File" }, returnLabels = {}, returnTypes = {}, userAccessible = true)
@UIExportPlugin(description = "Model files", extension = "model")
public class ExportClassifierToModel {

	@UITopiaVariant(affiliation = UITopiaVariant.EHV, author = "Hind", email = "Hind")
	@PluginVariant(requiredParameterLabels = { 0, 1 }, variantLabel = "Export Classifier to Model File")
	
	public void exportModel(PluginContext context, Classifier classifier, File file) throws Exception{
		
		/*http://weka.wikispaces.com/Serialization*/

		 weka.core.SerializationHelper.write(file.getAbsolutePath(), classifier);

	}
}
