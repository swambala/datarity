/**
 * @class Ext.app.PortalColumn
 * @extends Ext.container.Container
 * A layout column class used internally be {@link Ext.app.PortalPanel}.
 */
Ext.define('DATARITY.view.portal.PortalColumn', {
    extend: 'Ext.container.Container',
    alias: 'widget.portalcolumn',

    requires: ['DATARITY.view.portal.Portlet'],

    layout: 'anchor',
    defaultType: 'portlet',
    cls: 'x-portal-column'

    // This is a class so that it could be easily extended
    // if necessary to provide additional behavior.
});
