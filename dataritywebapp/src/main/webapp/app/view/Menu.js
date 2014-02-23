Ext.define('DATARITY.view.Menu', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.accpanel',
    layout: {
        type: 'accordion'//,
        //fill: false//,
        //titleCollapse: false//,
        //expandedItem: -1
    },

   /* layoutConfig: {
        titleCollapse: false,
        animate: false,
        activeOnTop: true,
        collapseFirst:true
    },*/
    iconCls: 'home',
    title: 'Datarity Menu',

    initComponent: function() {
        var me = this;

        me.callParent(arguments);
    }

});