/**
 * @class DATARITY.view.portlet.Portlet
 * @extends Ext.panel.Panel
 * A {@link Ext.panel.Panel Panel} class that is managed by {@link Ext.app.PortalPanel}.
 */
Ext.define('DATARITY.view.portal.Portlet', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.portlet',
    layout: 'fit',
    anchor: '100%',
    frame:false,
    closable: false,
    collapsible: false,
    animCollapse: false,
    draggable: false,
    /*draggable: {
        moveOnDrag: true    
    },*/
    //style:'maxWidth:265px !important',
    //style:'minHeight:170px !important',
    cls: 'x-portlet',

    // Override Panel's default doClose to provide a custom fade out effect
    // when a portlet is removed from the portal
    doClose: function() {
        if (!this.closing) {
            this.closing = true;
            this.el.animate({
                opacity: 0,
                callback: function(){
                    this.fireEvent('close', this);
                    this[this.closeAction]();
                },
                scope: this
            });
        }
    }
});
