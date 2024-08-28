require.config({
  resources: 'resources',
  paths: {
    jscore: '../jscore',
    widgets: '../widgets',
    chartlib: '../chartlib',
    text: '../jscore/require/text',
    styles: '../jscore/require/styles',
    template: '../jscore/require/template'
  },
  package: [
    {
      name: 'chartlib/widgets/DonutChart',
      main: 'DonutChart'
    }
  ]
});
