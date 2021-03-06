/**
 * Base plugin file that defines a visallo ui plugin.
 *
 * The main purpose is to apply less css to only the attachTo node.
 */
define([
    'flight/lib/component'
], function(defineComponent) {
    'use strict';

    var NODE_CLS_FOR_LESS_CONTAINMENT = 'less_cls_',
        componentInc = 0;

    return defineVisalloPlugin;

    function defineVisalloPlugin(Component, options) {

        console.warn('This component is deprecated as less in the client is deprecated');

        var FlightComponent = defineComponent.apply(null, [Component].concat(options && options.mixins || [])),
            attachTo = FlightComponent.attachTo,
            cls = NODE_CLS_FOR_LESS_CONTAINMENT + componentInc;

        if (options && options.less) {
            options.less.applyStyleForClass(cls);
        }

        FlightComponent.attachTo = function attachToWithLessClass(selector) {
            $(selector).each(function() {
                $(this).addClass(cls)
            });

            return attachTo.apply(this, arguments);
        };

        componentInc++;

        return FlightComponent;
    }
});
