define(['jscore/core',
    'template!./TableChart.html',
    'styles!./TableChart.less'
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
