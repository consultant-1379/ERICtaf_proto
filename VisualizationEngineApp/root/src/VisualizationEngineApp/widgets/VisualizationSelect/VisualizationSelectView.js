define([
	'jscore/core',
	'template!./VisualizationSelect.html',
	'styles!./VisualizationSelect.less'
], function(core, template, style) {
	
	return core.View.extend({
		getTemplate: function() {
			return template(this.options.presenter.getData());
		},
		
		getStyle: function() {
			return style;
		}
	});
});
