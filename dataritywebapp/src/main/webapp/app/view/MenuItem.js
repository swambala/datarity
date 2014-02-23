Ext.define ( 'DATARITY.view.MenuItem', {
    extend: 'Ext.tree.Panel' ,
    alias: 'widget.menuitem' ,
 
    border: 0,
    autoscroll: true ,
    title: 'test' ,
    rootVisible: false ,
 
    initComponent: function () {
        var  me = this ;
 
        me.callParent (arguments);
    }
 
});