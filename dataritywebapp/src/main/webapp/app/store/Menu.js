/*Ext.define ( 'DATARITY.store.Menu', {
    extend: 'Ext.data.Store' ,
 
    requires: [
        'DATARITY.model.MenuRoot'
    ], 
    constructor: function (cfg) {
        var  me = this ;
        cfg = cfg | | {};
        me.callParent ([Ext.apply ({
            storeId: 'MenuStore' ,
            model: 'DATARITY.model.MenuRoot' ,
            proxy: {
                type: 'ajax' ,
                url: 'server/menu.json' ,
                Reader: {
                    type: 'json' ,
                    root: 'items'
                }
            }
        }, cfg)]);
    }
});*/

Ext.define('DATARITY.store.Menu', {
    extend: 'Ext.data.Store',

    requires: [
        'DATARITY.model.MenuRoot'
    ],
    autoLoad : false,
    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            storeId: 'MenuStore',
            model: 'DATARITY.model.MenuRoot',
            proxy: {
                type: 'ajax',
                url: 'server/menu.json',
                reader: {
                    type: 'json',
                    root: 'items'
                }
            }
        }, cfg)]);
    }
});