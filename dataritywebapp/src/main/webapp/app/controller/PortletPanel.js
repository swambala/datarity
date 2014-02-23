Ext.define('DATARITY.controller.PortletPanel', {
    extend: 'Ext.app.Controller',

    stores: [
       
    ],

    views: [
        'PortletPanel'
    ],

    refs: [{
        ref: 'panel',
        selector: '',
        xtype: 'portletpanel',
        autoCreate: true
    }],

    init: function(application) {
        if (this.inited) {
            return;
        }
        this.inited = true;
        //console.log(this.control);
        this.control({
            'button[itemId=scanButton]': {
                click: 'onScaningProgress'
            }
           
        });
        scanReportAllDataStore.load();
    },

    onScaningProgress: function() {

        var PortletPanelController = this;

       // if(PortletPanelController.scanTimer != undefined)
         //clearInterval(PortletPanelController.scanTimer);

        if(PortletPanelController.scanButtonClick  == false || PortletPanelController.scanButtonClick  == undefined){        
            PortletPanelController.scanButtonClick  =true;
            scanTimer=setTimeout(function(){PortletPanelController.onScaningProgressStop()},10000);
            Ext.getCmp('scanProgressBarContainer').setVisible(true);
            var scanProgressBar= Ext.getCmp('scanProgressBarId');
            scanProgressBar.wait({
                interval: 1000, //bar will move fast!
                duration: 10000,
                increment: 15,
                text: 'Scaning...',
                scope: this,
                fn: function(){
                    scanProgressBar.updateText('Done!');
                }
            });
        }
    },
    onScaningProgressStop: function(){
        var PortletPanelController = this;
        var url = './server/dataritysample_2.json';
        if(scanReportAllDataStore.getProxy().url == './server/dataritysample_2.json')
            url = './server/dataritysample_1.json';

        scanReportAllDataStore.getProxy().url = url;
        scanReportAllDataStore.load();

        Ext.getCmp('scanProgressBarContainer').setVisible(false);
        PortletPanelController.scanButtonClick =false;
    },

    /*onScaningProgress: function() {

        var PortletPanelController = this;

        //if(PortletPanelController.myVar != undefined)
         //clearInterval(PortletPanelController.myVar);

        
        myVar=setTimeout(function(){PortletPanelController.onScaningProgressStop()},10000);
        Ext.getCmp('scanProgressBarContainer').setVisible(true);
        var scanProgressBar= Ext.getCmp('scanProgressBarId');
        scanProgressBar.wait({
            interval: 1000, //bar will move fast!
            duration: 10000,
            increment: 15,
            text: 'Scaning...',
            scope: this,
            fn: function(){
                scanProgressBar.updateText('Done!');
            }
        });
    },
    onScaningProgressStop: function(){
        var PortletPanelController = this;
        var url = './server/dataritysample_2.json';
        if(scanReportAllDataStore.getProxy().url == './server/dataritysample_2.json')
            url = './server/dataritysample_1.json';

        scanReportAllDataStore.getProxy().url = url;
        scanReportAllDataStore.load();

        Ext.getCmp('scanProgressBarContainer').setVisible(false);
       // clearInterval(PortletPanelController.myVar);
    },*/
    /*onScanFinishProgress:function(){
        alert(1);
    },*/
    loadView: function() {
        this.application.setMainView(this.getPortletPanelView());
    }
});