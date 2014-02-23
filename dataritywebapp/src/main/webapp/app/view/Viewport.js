Ext.define('DATARITY.view.Viewport', {
    extend: 'Ext.container.Viewport',

    layout: {
        type: 'border'
    },
	uses: [
        'DATARITY.view.PortletPanel'
    ],
    requires: [
        'DATARITY.view.Menu',
        'DATARITY.view.MenuItem'
    ],
    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'box',
                    id: 'datarityHeaderBox',
                    region: 'north',
                    contentEl:'datarityHeader',
                   // height: 80
                },{
                    id: 'app-portal',
                    itemId:'dashboardbox',
                    border:false,
                    autoScroll: true,
                    xtype: 'panel',
                    region: 'center',
                },{
                    xtype : 'accpanel',
                    itemId: 'accpanel',
                    //id:'accpanel1',
                    width: 200,
                    minWidth: 175,
                    maxWidth: 400,
                    titleCollapse: false,
                    region: 'west',
                    split: true,
                    collapsible: true,
                    collapseMode: 'mini',
                    autoScroll: true,
                    collapseFirst :true,
                    titleCollapse:true,
                    listeners: {
                       afterrender : function () {

                       }
                    }
                }/*{
                    id: 'datarity-accpanel',
                    //split: true,
                    width: 200,
                    minWidth: 175,
                    maxWidth: 400,
                    titleCollapse: false,
                    region: 'west',
                    split: true,
                    collapsible: true,
                    collapseMode: 'mini',
                    autoScroll: true,
                    layout: 'accordion',
                    items: [{
                        //contentEl: 'west',
                       // html : 'test',
                        title: 'Dashboard',
                        iconCls: 'dashboard',
                    }, {
                        title: 'Scan',
                        html: '<p>Some settings in here.</p>',
                        iconCls: 'scan'
                    }, {
                        title: 'Mask',
                        html: '<p>Some info in here.</p>',
                        iconCls: 'mask'
                    }]
                },*/
                
            ]
        });

        me.callParent(arguments);
    }
});