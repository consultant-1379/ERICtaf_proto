
/*global define*/
define('widgets/utils/dataNameUtils',[
    'jscore/core'
], function (core) {
    

    return {
        prefix: '',

        translate: function(prefix, template, view) {
            if ((typeof template) === 'string') {
                template = core.Element.parse(template);
            }
            this.prefix = prefix;
            this.translateElement.call(this, template, view, true);
            this.translateChildren.call(this, template, view);
            return template;
        },

        translateElement: function(element, view, isRoot) {
            if (isRoot) {
                if (this.prefix === undefined || this.prefix === null) {
                    this.prefix = element.getAttribute('data-namespace');
                    element._getHTMLElement().removeAttribute('data-namespace');
                    element.setAttribute('class', this.prefix);
                }
            }

            var name = element.getAttribute('data-name');
            var type = element.getAttribute('data-type');

            var classes = element.getAttribute('class');

            if (classes !== undefined) {
                classes = classes.split(' ');
            } else {
                classes = [];
            }

            if (type) {
                classes.push(this.namespace + '-' + type);
                element._getHTMLElement().removeAttribute('data-type');
                if (view[type] === undefined) {
                    view[type] = [];
                }

                view[type].push(element);
            }

            if (name) {
                classes.push(this.prefix + '-' + name);
                // Prevents a conflicts with existing methods
                if (view[name] === undefined) {
                    view[name] = element;
                } else {
                    throw new Error('Name already in use: ' + name);
                }
                element._getHTMLElement().removeAttribute('data-name');
            }

            element.setAttribute('class', classes.join(' '));

            if (isRoot && name) {
                this.prefix = this.prefix + '-' + name;
            }
        },

        translateChildren: function(template, view) {
            var children = template.children();

            for (var i = 0, l = children.length; i < l; i++) {
                var child = children[i];
                this.translateElement.call(this, child, view, false);

                if (child.children().length > 0) {
                    this.translateChildren.call(this, child, view);
                }
            }
        }
    };

});
