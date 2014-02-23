Ext.define ( 'DATARITY.model.MenuRoot', {
    extend: 'Ext.data.Model' ,
 
    uses: [
        'DATARITY.model.MenuItem'
    ],
 
    idProperty: 'id' ,
 
    fields: [
        {
            name: 'title'
        },
        {
            name: 'iconCls'
        },
        {
            name: 'id'
        }
    ],
 
    hasMany: {
        model: 'DATARITY.model.MenuItem' ,
        foreignKey: 'menu_id' ,
        name: 'items'
    }
});