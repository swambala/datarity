Ext.define('DATARITY.controller.Viewport', {
    extend: 'Ext.app.Controller',

   models: [
        'MenuRoot',
        'MenuItem'
    ],
   stores: [
        'Menu'
    ],
   views: [
        'Menu',
        'MenuItem'
    ],


    onPanelRender: function(abstractcomponent, options) {
        
        this.getMenuStore().load(function(records, op, success){

            var menuPanel = Ext.ComponentQuery.query('accpanel')[0];

            Ext.each(records, function(root){

                /*if(root.get('id') == "dashboard_tree") {
                     var menu = Ext.create('DATARITY.view.MenuItem',{
                        title: root.get('title'),
                        hideCollapseTool:true,
                        titleCollapse: false,
                        fill:false,
                        iconCls: root.get('iconCls')
                    });
                } else {*/
                    var menu = Ext.create('DATARITY.view.MenuItem',{
                        title: root.get('title'),
                        iconCls: root.get('iconCls')
                    });

                    Ext.each(root.items(), function(itens){

                        Ext.each(itens.data.items, function(item){

                            menu.getRootNode().appendChild({
                                text: item.get('text'), 
                                leaf: true, 
                                iconCls: item.get('iconCls'),
                                id: item.get('id'),
                                className: item.get('className') 
                            });
                        });  
                    });
                //}
                menuPanel.add(menu);
            }); 
        });
    },

    onTreepanelSelect: function(selModel, record, index, options) {
        Ext.Msg.alert('You selected the following menu item', record.get('text'));

    },

    init: function(application) {
        if (this.inited) {
            return;
        }
        this.inited = true;
        this.control({
            'accpanel': {
                beforerender: this.onPanelRender
            },
            "viewport treepanel": {
                select: this.onTreepanelSelect
            }
        });
        this.onPanelRender();

        this.application.addController("PortletPanel");
    }
});