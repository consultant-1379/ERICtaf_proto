define([
	'jscore/core',
	'./VisualizationSelectView',
	'widgets/Spinner'
], function(core, VisualizationSelectView, Spinner) {
	
	return core.Widget.extend({
		View: VisualizationSelectView,
		
		init: function() {

		},
		
		onViewReady: function() {
			var spinnerHolder = this.getElement().find('.eaVisualizationEngineApp-wVisualizationSelect-spinner');
			
			this.spinnerWidget = new Spinner({
				value: 1,
				max: 12,
				min: 1,
				postfix: ' cols',
				prefix: this.options.title
			});
			
			this.spinnerWidget.attachTo(spinnerHolder);
		},
		
		getData: function() {
			return this.options;
		},
		
		getOptionsAsJSONString: function() {
			var options = {
				factory: this.options.factory,
				span: this.spinnerWidget.getValue(),
				title: this.options.title
			};
			
			return JSON.stringify(options);
		}
	});
});
