
     Ext.define('scanReportAllData', {
        extend: 'Ext.data.Model',
        fields: [
            {name: 'id'},
            {name: 'totalSecurityEscape',  type: 'int'},
            {name: 'totalCreditCardNum', mapping: 'totalCreditCardNum', type: 'int'},
            {name: 'totalPhoneNum', mapping: 'totalPhoneNum', type: 'int'},
            {name: 'totalEmailId',  type: 'int'},
            {name: 'totalSsnNum',  type: 'int'},
            {name: 'fileNameToSecurityEscape'},
            {name: 'fileNameToSecurityTypeToEscape'}
        ]
    });

    // create the Data Store
    var scanReportAllDataStore = Ext.create('Ext.data.Store', {
        id: 'scanReportAllDataStore',
        model: 'scanReportAllData',
        autoLoad:false,
        proxy: {
            type: 'ajax',
            actionMethods: {
                read: 'GET'
            },
            url: './server/dataritysample_1.json',
            extraParams: {
                todo : 'getChartDatas'
            },
            reader: {
                type: 'json',
                root: 'results',
                totalProperty: 'totalCount'
            },
            simpleSortMode: true
        },
        listeners: {
            load: function(store, records, success) {
                 var fileNameToSecurityEscape = records[0]['data']['fileNameToSecurityEscape'];

                var scanReportColumnData = [];
                var keySelected = "";
                Ext.Object.each(fileNameToSecurityEscape,function(key,value) {
                    var keyValArr = key.split("/");

                    var name    = keyValArr[keyValArr.length-1];
                    var data    = value;

                    if(keySelected == "")
                        keySelected = name;

                    scanReportColumnData.push({"name":name,"data":data});
                });

                scanReportColumnDataStore.loadData(scanReportColumnData);

                var fileNameToSecurityTypeToEscapeArr = scanReportAllDataStore.data.items[0].data.fileNameToSecurityTypeToEscape;

                var pieChartDataArr = fileNameToSecurityTypeToEscapeArr[keySelected];

                var scanReporPieData = [];
                Ext.Object.each(pieChartDataArr,function(key,value) {
                     var name    = key;
                    if(key == "_EMAIL")
                        name    = "Email Id";
                    if(key == "_SSN")
                        name    = "SSN";
                    if(key == "_PHONE")
                        name    = "Ph No";
                    if(key == "_CREDITCARD")
                        name    = "Credit";

                    var data    = value;

                    scanReporPieData.push({"name":name,"data":data});
                });

                scanReportDataStore.loadData(scanReporPieData);
                Ext.get("scanReportPieChartLblTitle").update("File Results of "+keySelected);

                var series = scanReportColumnChart.series.get(0);

                if(selectedStoreItem == false) {
                    selectedStoreItem = series.items[0].storeItem;
                    selectItem(selectedStoreItem);
                }
                /*else if( series.items[0].storeItem.get("name") != selectedStoreItem.get("name")) {
                    selectedStoreItem = series.items[0].storeItem;   
                    selectItem(selectedStoreItem);
                }*/
                var creditcard_portletData = {totalCreditCardNum: DATARITY.app.formatNumber(scanReportAllDataStore.data.items[0].data.totalCreditCardNum)};
                Ext.getCmp("creditcard_portlet").update(creditcard_portletData);

                var ssn_portletData = {totalSsnNum: DATARITY.app.formatNumber(scanReportAllDataStore.data.items[0].data.totalSsnNum)};
                Ext.getCmp("ssn_portlet").update(ssn_portletData);

                var phoneno_portletData = {totalPhoneNum: DATARITY.app.formatNumber(scanReportAllDataStore.data.items[0].data.totalPhoneNum)};
                Ext.getCmp("phoneno_portlet").update(phoneno_portletData);

                var email_colData = {totalEmailId: DATARITY.app.formatNumber(scanReportAllDataStore.data.items[0].data.totalEmailId)};
                Ext.getCmp("email_col").update(email_colData);

            }
        }
    });

   
    Ext.define('scanReportData', {
        extend: 'Ext.data.Model',
        fields: [
            {name: 'name', mapping: 'name', type: 'string'},
            {name: 'data',  type: 'int'}
        ]
    });

    // create the Data Store
    var scanReportDataStore = Ext.create('Ext.data.Store', {
        id: 'scanReportDataStore',
        model: 'scanReportData',
        autoLoad:false,
        simpleSortMode: true,
        data : []
       /* proxy: {
            type: 'ajax',
            actionMethods: {
                read: 'POST'
            },
            url: './server/scanreport_piedata.json',
            extraParams: {
                todo : 'Get_ScanResults'
            },
            reader: {
                type: 'json',
                root: 'SCANRESULTS',
                totalProperty: 'totalCount'
            },
            simpleSortMode: true
        }*/
    });

    var donut = false;
    
    var scanReportPieChart = Ext.create('Ext.chart.Chart', {
        xtype: 'chart',
        animate: true,
        store: scanReportDataStore,
        shadow: true,
        legend: {
            position: 'right'
        },
        insetPadding: 60,
         width:500,
        height:350,
        theme: 'Base:gradients',
        series: [{
            type: 'pie',
            //field: 'data',
            angleField: 'data',
            showInLegend: true,
            donut: 35,
            tips: {
                trackMouse: true,
                width: 140,
                height: 28,
                renderer: function(storeItem, item) {
                    //calculate percentage.
                    var total = 0;
                    scanReportDataStore.each(function(rec) {
                        total += rec.get('data');
                    });
                    this.setTitle(storeItem.get('name') + ': ' + Math.round(storeItem.get('data') / total * 100) + '%');
                }
            },
            highlight: {
                segment: {
                    margin: 20
                }
            },
            label: {
                field: 'name',
                display: 'rotate',
                contrast: true,
                font: '18px Arial'
            }
        }],
        listeners : {
            click : function(_) {

            }
        }
    });

Ext.define('scanReportColumnData', {
        extend: 'Ext.data.Model',
        fields: [
            {name: 'name', mapping: 'name', type: 'string'},
            {name: 'data',  type: 'int'}
        ]
    });

    // create the Data Store
    var scanReportColumnDataStore = Ext.create('Ext.data.JsonStore', {
        id: 'scanReportColumnDataStore',
        model: 'scanReportColumnData',
         simpleSortMode: true,
        data : []
       /* autoLoad:true,
        proxy: {
            type: 'ajax',
            actionMethods: {
                read: 'POST'
            },
            url: './server/scanreport_columndata.json',
            extraParams: {
                todo : 'Get_ScanResults'
            },
            reader: {
                type: 'json',
                root: 'SCANCOLUMNRESULTS',
                totalProperty: 'totalCount'
            },
            simpleSortMode: true
        }*/
    });

    var selectedStoreItem = false;
    //performs the highlight of an item in the bar series
    var selectItem = function(storeItem) {
        var name = storeItem.get('name'),
            series = scanReportColumnChart.series.get(0),
            i, items, l;
        
        series.highlight = true;
        series.unHighlightItem();
        series.cleanHighlights();
        for (i = 0, items = series.items, l = items.length; i < l; i++) {
            if (name == items[i].storeItem.get('name')) {
                selectedStoreItem = items[i].storeItem;
                series.highlightItem(items[i]);
                break;
            }
        }
        series.highlight = false;
    };

 var scanReportColumnChart = Ext.create('Ext.chart.Chart', {
        //style: 'background:#fff',
        animate: true,
        shadow: true,
        border:false,
         width:500,
         height:350,
         cls: 'x-panel-body-default',
        store: scanReportColumnDataStore,

        axes: [{
            type: 'Numeric',
            position: 'left',
            fields: ['data'],
            title: 'Number of PIIs',
            grid: true,
            minimum: 0
        }, {
            type: 'Category',
            position: 'bottom',
            fields: ['name'],
            title: 'Directory Report',
             label: {
               // renderer: Ext.util.Format.numberRenderer('0,0')
                renderer: function(v) {
                    return Ext.String.ellipsis(v, 15, false);
                },
                rotate: {
                    degrees: 270
                }
            },
        }],
        series: [{
            type: 'column',
            axis: 'left',
            
            highlight: false,
            style: {
                fill: '#456d9f'
            },
           
            tips: {
                trackMouse: true,
                width: 140,
                height: 28,
                renderer: function(storeItem, item) {
                    this.setTitle(storeItem.get('name') + ': ' + storeItem.get('data'));
                }
            },
            label: {
                display: 'insideEnd',
                'text-anchor': 'middle',
                field: 'data',
                renderer: Ext.util.Format.numberRenderer('0'),
                orientation: 'vertical',
                color: '#333'
            },
            listeners: {
                itemmouseup: function(item) {
                     if(selectedStoreItem == false) 
                         selectedStoreItem = item.storeItem;
                    else if(item.storeItem.get("name") == selectedStoreItem.get("name"))
                        return true;

                    //if(item.storeItem.get("name") != selectedStoreItem.get("name")) {
                        selectedStoreItem = item.storeItem;

                        var valueArr = item.value;
                        var name    = valueArr[0];
                        
                        var fileNameToSecurityTypeToEscapeArr = scanReportAllDataStore.data.items[0].data.fileNameToSecurityTypeToEscape;

                        var pieChartDataArr = fileNameToSecurityTypeToEscapeArr[name];

                        var scanReporPieData = [];
                        Ext.Object.each(pieChartDataArr,function(key,value) {
                             var name    = key;
                            if(key == "_EMAIL")
                                name    = "Email Id";
                            if(key == "_SSN")
                                name    = "SSN";
                            if(key == "_PHONE")
                                name    = "Ph No";
                            if(key == "_CREDITCARD")
                                name    = "Credit";

                            var data    = value;

                            scanReporPieData.push({"name":name,"data":data});
                        });

                        Ext.get("scanReportPieChartLblTitle").update("File Results of "+name);

                        scanReportDataStore.loadData(scanReporPieData);

                        selectItem(selectedStoreItem);
                    //}
                }/*,
                itemmousedown: function(el) {
                    var series = this.chart.series.get(0);
                    series.highlight = true;
                    series.unHighlightItem();
                    series.cleanHighlights();

                    series.highlightItem(el);
                    series.highlight = false;
                }*/
            },
            xField: 'name',
            yField: 'data'
        }]
    });

   
Ext.define('DATARITY.view.PortletPanel', {
    extend: 'DATARITY.view.portal.PortalPanel',    
    
    alias: 'widget.portletpanel',
    border:false,
     
    getTools: function(){
        return [{
            xtype: 'tool',
            type: 'gear',
            handler: function(e, target, panelHeader, tool){
                var portlet = panelHeader.ownerCt;
                portlet.setLoading('Loading...');
                Ext.defer(function() {
                    portlet.setLoading(false);
                }, 2000);
            }
        }];
    },
    
    initComponent: function() {

        var scanProgressBar = Ext.create('Ext.ProgressBar', {
           //renderTo: Ext.getBody(),
           width: 300,
           id:'scanProgressBarId',
           itemId:'scanProgressBarItemId'
        });

       var creditcard_tpl =  new Ext.XTemplate(
                        '<div><div class="dashboard-stat blue" style="background-color: #27a9e3;">',
                        '<div class="visual"><i class="fa fa-credit-card"></i></div>',
                        '<div class="details"><div class="number">{totalCreditCardNum} +</div><div class="desc">Credit Cards</div></div>',
                        '<a class="more" href="#">View details <i class="m-icon-swapright m-icon-white"></i></a></div></div>',
                        { compiled : true }

                    );
       var ssn_tpl =  new Ext.XTemplate(
                        '<div><div class="dashboard-stat green" style="background-color: #28b779;">',
                        '<div class="visual"><i class="fa fa-users"></i></div>',
                        '<div class="details"><div class="number">{totalSsnNum} +</div><div class="desc">SSNs</div></div>',
                        '<a class="more" href="#">View details <i class="m-icon-swapright m-icon-white"></i></a></div></div>',
                        { compiled : true }

                    );
       var phoneno_tpl =  new Ext.XTemplate(
                        '<div><div class="dashboard-stat purple" style="background-color: #852b99;">',
                        '<div class="visual"><i class="fa fa-phone"></i></div>',
                        '<div class="details"><div class="number">{totalPhoneNum} +</div><div class="desc">Phone Numbers</div></div>',
                        '<a class="more" href="#">View details <i class="m-icon-swapright m-icon-white"></i></a></div></div>',
                        { compiled : true }

                    );
       var email_tpl =  new Ext.XTemplate(
                        '<div> <div class="dashboard-stat yellow" style="background-color: #ffb848;">',
                        '<div class="visual"><i class="fa fa-envelope"></i></div>',
                        '<div class="details"><div class="number">{totalEmailId}</div><div class="desc">EMail Ids</div></div>',
                        '<a class="more" href="#">View details <i class="m-icon-swapright m-icon-white"></i></a></div></div>',
                        { compiled : true }

                    );
        Ext.apply(this, {

            items: [{
                id: 'creditcard_col',
                columnWidth: 0.25,
                width:250,
                items: [{
                    id: 'creditcard_portlet',
                    tpl:creditcard_tpl,
                    data:'',
                    listeners : {
                        afterrender : function() {
                             this.update();
                        }
                    }
                }]
            },{
                id: 'ssn_col',
                columnWidth: 0.25,
                items: [{
                    id: 'ssn_portlet',
                    tpl:ssn_tpl,
                    data:'',
                    listeners : {
                        afterrender : function() {
                             this.update();
                        }
                    }
                }]
            },{
                id: 'phoneno_col',
                 columnWidth: 0.25,
                items: [{
                    id: 'phoneno_portlet',
                    tpl:phoneno_tpl,
                    data:'',
                    listeners : {
                        afterrender : function() {
                             this.update();
                        }
                    }
                }]
            },{
                id: 'email_col',
                columnWidth: 0.25,
                tpl:email_tpl,
                data:'',
                listeners : {
                    afterrender : function() {
                         this.update();
                    }
                }
            },{
                xtype : 'container',
                columnWidth: 1,
               align : 'center',
                border:true,
                layout: {
                    type: 'hbox',
                    align: 'stretch'
                },
                style : 'padding:20px 0px 20px 0px;',
                items: [{
                    xtype : 'tbspacer',
                    border:false,
                    flex:3
                },{
                   flex : 1,
                    xtype : 'button',
                    itemId:'scanButton',
                    width : 150,
                    frame : true,
                    scale   : 'large',
                    text : 'Scan'/*,
                    handler : function () {
                        scanReportAllDataStore.getProxy().url = './server/dataritysample_2.json';
                        scanReportAllDataStore.load();
                    }*/
                },{
                    xtype : 'tbspacer',
                    border:false,
                    width:30
                },{
                    flex : 1,
                    xtype : 'button',
                    width : 150,
                    frame : true,
                    scale   : 'large',
                    text : 'Mask'
                },{
                    xtype : 'tbspacer',
                    border:false,
                    flex:3
                }]
            },{
                xtype : 'container',
                id:'scanProgressBarContainer',
                itemId:'scanProgressBarContainerItemId',
                columnWidth: 1,
                //hidden:true,
                align : 'center',
                border:true,
                layout: {
                    type: 'hbox',
                    align: 'stretch'
                },
                style : 'padding:20px 0px 20px 0px;',
                items: [{
                    xtype : 'tbspacer',
                    border:false,
                    flex:3
                },scanProgressBar,
                /*{
                    xtype:'panel',
                    html:'test'
                },*/
                {
                    xtype : 'tbspacer',
                    border:false,
                    width:30
                },,{
                    xtype : 'tbspacer',
                    border:false,
                    flex:3
                }]
            },{
                xtype:'panel',
                columnWidth: 0.5,
                //  width: 800,
                height: 350,
                border:false,
                //title: 'Scan Results',
                items:scanReportColumnChart //,
               /* tbar: [{
                        enableToggle: true,
                        pressed: false,
                        text: 'Donut',
                        toggleHandler: function(btn, pressed) {
                            scanReportPieChart.series.first().donut = pressed ? 35 : false;
                            scanReportPieChart.refresh();
                        }
                }]*/
            },{
                xtype:'panel',
                columnWidth: 0.5,
                //  width: 800,
                border:false,
                height: 350,
                //title: 'Scan Results 2',
                items: [ { 
                        xtype:'label',
                       // text:'',
                       html  :'<div class="portlet-title">'+
                            '<div class="caption" id="scanReportPieChartLblTitle">'+
                                'File Results'+
                            '</div>'+
                        '</div>',
                       // html  :'<text zIndex="0" x="0" y="0" text="Directory Report" font="bold 18px Arial" fill="#444" text-anchor="start" transform="matrix(1,0,0,1,218,333.8)" style="-webkit-tap-highlight-color: rgba(0, 0, 0, 0); font-weight: bold; font-style: normal; font-variant: normal; font-size: 18px; line-height: normal; font-family: Arial;"><tspan x="0" dy="5.25">Directory Report</tspan></text>',
                        itemId : 'scanReportPieChartLbl',
                        width : 400
                    },scanReportPieChart]
            }]
            
        });
                
        this.callParent(arguments);
        Ext.getCmp('scanProgressBarContainer').setVisible(false)
    }
});
