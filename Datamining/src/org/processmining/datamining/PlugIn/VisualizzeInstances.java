package org.processmining.datamining.PlugIn;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.contexts.util.StringVisualizer;
import org.processmining.framework.plugin.Progress;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;

import com.fluxicon.slickerbox.factory.SlickerDecorator;

import weka.core.Instances;


@Visualizer
@Plugin(name = "Visualizer Instances Weka", parameterLabels = "Visualizze Instances Weka", returnLabels = "Instances Weka Visualized", returnTypes = JComponent.class)

public class VisualizzeInstances {

	
	@PluginVariant(requiredParameterLabels = { 0 },variantLabel="Visualizer Instances Weka")
	@UITopiaVariant(affiliation = UITopiaVariant.EHV, author = "Hind", email = "di.unipi.it")
	public JComponent visualize(UIPluginContext context, Instances instace) {
		String prima = instace.toString();
		String seconda = prima.replace("\n", "<br/>");
		JComponent panel = visualizestring("<html>"+seconda+"</html>");
		
		return panel;
	}
	
	public static JComponent visualizestring( String tovisualize) {
		JScrollPane sp = new JScrollPane();
		sp.setOpaque(false);
		sp.getViewport().setOpaque(false);
		sp.setBorder(BorderFactory.createEmptyBorder());
		sp.setViewportBorder(BorderFactory.createLineBorder(new Color(10, 10, 10), 2));
		sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		SlickerDecorator.instance().decorate(sp.getVerticalScrollBar(), new Color(0, 0, 0, 0),
				new Color (140, 140, 140), new Color(80, 80, 80));
		sp.getVerticalScrollBar().setOpaque(false);
		
		
		JLabel l = new JLabel(tovisualize);
		sp.setViewportView(l);

		return sp;
	}


	
}
