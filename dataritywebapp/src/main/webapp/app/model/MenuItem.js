Ext.define ( 'DATARITY.model.MenuItem', {
    extend: 'Ext.data.Model' ,
 
    uses: [
        'DATARITY.model.MenuRoot'
    ],
 
    idProperty: 'id' ,
 
    fields: [
        {
            name: 'text'
        },
        {
            name: 'iconCls'
        },
        {
            name: 'className'
        },
        {
            name: 'id'
        },
        {
            name: 'menu_id'
        }
    ],
 
    belongsTo: {
        model: 'DATARITY.model.MenuRoot' ,
        foreignKey: 'menu_id'
    }
});