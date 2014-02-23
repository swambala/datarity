Ext.application({
    autoCreateViewport: true,
    name: 'DATARITY',

    launch: function() {

        this.viewport = Ext.ComponentQuery.query('viewport')[0];
        this.centerPanel = Ext.ComponentQuery.query('#dashboardbox')[0]
        var c = this.getController('Viewport');
        c.init();
    },
    
    addController:function(controllerName, actionName){
        var controller = this.getController(controllerName);
        controller.init(this);
        controller.loadView();
    },

    removeController:function(controllerName){
        //console.log(this.controllers);
        var controller  = this.getController(controllerName),
            controllers = this.controllers,
            className   = Ext.ClassManager.getName(controller);

        
        //remove controller listeners
        Ext.app.EventBus.unlisten(controller.id);
        
        //remove controller from app
        Ext.destroy(controllers.remove(controller));

        //clean up the class name
        Ext.ClassManager.setNamespace(className, null);
        
        //console.log(this.controllers);


    },
   setMainView: function(view){
        var center = this.centerPanel;
        if(!center.contains(view)) {
            center.add(view);
        }
       //center.setActiveTab(view);
       center.doLayout();
    },
    
    formatNumber : function(n){

        var ranges = [
              { divider: 1e18 , suffix: 'P' },
              { divider: 1e15 , suffix: 'E' },
              { divider: 1e12 , suffix: 'T' },
              { divider: 1e9 , suffix: 'G' },
              { divider: 1e6 , suffix: 'M' },
              { divider: 1e3 , suffix: 'k' }
        ];
        for (var i = 0; i < ranges.length; i++) {
            if (n >= ranges[i].divider) {
                return (Math.round(n / ranges[i].divider)).toString() + ranges[i].suffix;
            }
        }
      return n.toString();
    }
});