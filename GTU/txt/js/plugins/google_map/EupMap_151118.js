// ** EupMap Constructor **
function EupMap(gisX, gisY, zoom, divId, EupMapStyleTypeId) {
    //Trung
    var flightPlanCoordinates = [];
    var flightPath = new google.maps.Polyline({
        path: flightPlanCoordinates,
        geodesic: true,
        strokeColor: '#CC0066',
        strokeOpacity: 1.0,
        strokeWeight: 4
    });
    var numDeltas = 100;
    var delay = 10; //milliseconds
    var latCurrent;
    var lngCurrent;

    var nextIndex = 0;
    var deltaLat;
    var deltaLng;
    var currentUpdateMarker;
    //End Trung

    this._cursorX = 0;
    this._cursorY = 0;
    /* ThiemNN */
    this._car_Unicode = null;
    /* End ThiemNN */

    this._map = new google.maps.Map(
        document.getElementById(divId), {
            zoom: zoom,
            center: new google.maps.LatLng(gisY, gisX),
            mapTypeId: google.maps.MapTypeId.ROADMAP,
            disableDoubleClickZoom: true,
            scaleControl: true,
            gestureHandling: 'greedy',
            scaleControlOptions: {
                position: google.maps.ControlPosition.RIGHT_BOTTOM
            }
        }
    )
    google.maps.controlStyle = 'azteca';
    var rendererOptions = {
        suppressMarkers: true
    }
    this.directionsService = new google.maps.DirectionsService; /*路徑規劃服務*/
    this.streetviewService = new google.maps.StreetViewService(); /*街景服務*/
    this.DIRECTION_WAYPOINTS_LIMIT = 8; /*路徑規劃中繼點數量限制*/
    if (EupMapStyleTypeId != undefined && EupMapStyleTypeId != null)
        this._map.setOptions({
            styles: new EupMapStyleType().GetEupMapStyleById(EupMapStyleTypeId)
        });
    this._trafficLayer = new google.maps.TrafficLayer();
    this._allRoutes = new Array(); /*路線規劃 add by yorick 20160628*/
    this._allMarkers = new Array();
    this._stopMarkers = new Array();
    this._pointMarkers = new Array();
    this._allLabels = new Array();
    this._allLines = new Array();
    this._allPolygon = new Array();
    this._allTargetLabels = new Array();
    this._allTargetMarkers = new Array(); /*LocTarget add by yorick 20160322*/
    this._allCircle = new Array();
    this._directionRequestPool = new Array(); /*路線規劃結果暫存空間*/
    this._distanceMarkers = new Array();
    this._distanceLines = new Array();
    this._polylineWithArrow = new Array();
    this._forbiddenRoute = new Array(); // 禁行路段
    this._deleteMarkers = new Array();
    this._undoMarkers = new Array();

    this.IsEnabledLocateInfo = false; //是否啟用 定位資訊框
    this.LocateMarkerIndex = null; //DBClick定位之Marker
    this.LccateStreetView = null; //定位之街景縮圖

    this.LandmarkInfowindow = null; //地標資訊 infowindow
    //this.CarInfowindow = null; //地標資訊 infowindow
    this.SelfSiteInfowindow = null; //自建工地資訊 infowindow
    this._nowPolygonPoints = [];
    this._nowCirclePoints = [];

    // **************Function***************
    /*ThiemNN */
    EupMap.GetCarTodayData = function (car_Unicode) {
        try {
            var _carTodayStatis = EUP.GetModel('GetCarTodayStatis').SetParameter({
                Car_Unicode: car_Unicode
            }).Exec();
        } catch (e) {
            console.log(e.message);
        }
    }
    /*End ThiemNN */

    if (typeof EupMap._initialized == "undefined") {
        // ****方法:由外部偵測游標最新位置透過此方法告知 *****
        EupMap.prototype.NotifyCursorXY = function (new_cursorX, new_cursorY) {
            this._cursorX = new_cursorX;
            this._cursorY = new_cursorY;
        };

        // ****方法:取得單點街景 *****
        EupMap.prototype.GetPointStreetView = function (gisx, gisy, container) {
            var that = this;
            var point = new google.maps.LatLng(gisy, gisx);
            var panorama = new google.maps.StreetViewPanorama(container, {
                addressControl: false,
                fullscreenControl: false,
                disableDefaultUI: false,
                imageDateControl: false,
                linksControl: false,
                scrollwheel: false,
                zoomControl: false,
                panControl: false,
                clickToGo: false
            });
            var processSVData = function (data, status) {
                if (status === google.maps.StreetViewStatus.OK) {
                    panorama.setPano(data.location.pano);
                    panorama.setPov({
                        heading: 270,
                        pitch: 0
                    });
                    panorama.setVisible(true);
                } else {
                    console.error('Street View data not found for this location.');
                }
            }
            this.streetviewService.getPanorama({
                location: point,
                radius: 50
            }, processSVData);
        }

        //**方法：路徑規劃**
        EupMap.prototype.CalcRoute = function (route_index, directionsDisplay, pFrom, pEnd, waypts, callback) {
            var that = this;
            var start = pFrom;
            var end = pEnd;
            var request = {
                origin: start,
                destination: end,
                optimizeWaypoints: true,
                waypoints: waypts,
                travelMode: google.maps.DirectionsTravelMode.DRIVING
            };
            var check_request = function (request) {
                for (var i = that._directionRequestPool.length - 1; i >= 0; i--) {
                    var single = that._directionRequestPool[i];
                    if (JSON.stringify(single.request) == JSON.stringify(request))
                        return single.response;
                }
                return null;
            }
            var find_response = check_request(request);
            var do_draw = function (response) {
                directionsDisplay.setDirections(response);
                /*畫一條蓋在路徑規劃上的線 for EventOnLine*/
                response.Line_Color = directionsDisplay.Line_Color;
                var routLine = that.DrawPolyLineOnRoute(response);
                if (directionsDisplay.hasOwnProperty('RouteLine')) {
                    directionsDisplay.RouteLine.setMap(null);
                }
                directionsDisplay.RouteLine = routLine;
                if (callback) {
                    try {
                        callback(route_index, directionsDisplay.getDirections());
                    } catch (e) {
                        console.log(e.message)
                    };
                }
            }
            if (find_response == null) {
                this.directionsService.route(request, function (response, status) {
                    if (status == google.maps.DirectionsStatus.OK) {
                        that._directionRequestPool.push({
                            request: request,
                            response: response
                        });
                        do_draw(response);
                    } else if (status == google.maps.DirectionsStatus.OVER_QUERY_LIMIT) {
                        var retry = function () {
                            that.CalcRoute(route_index, directionsDisplay, pFrom, pEnd, waypts, callback);
                        }
                        setTimeout(retry, 10);
                    } else {
                        console.log(status);
                    }
                });
            } else {
                do_draw(find_response);
            }
        }


        //**方法：新增路徑規劃**
        EupMap.prototype.AddRoute = function (start_gisx, start_gisy, end_gisx, end_gisy, draggable, route_color, line_color, waypoints, callback) {
            var start = new google.maps.LatLng(start_gisy, start_gisx);
            var end = new google.maps.LatLng(end_gisy, end_gisx);
            var route_color = route_color || "#FF000";
            var line_color = line_color || "blue";
            var draggable = draggable ? true : false;
            var directionsDisplay = new google.maps.DirectionsRenderer({
                polylineOptions: {
                    strokeColor: route_color,
                    strokeOpacity: 1,
                    strokeWeight: 5,
                    zIndex: 1
                },
                suppressMarkers: true,
                // suppressPolylines: true,
                map: this._map,
                draggable: draggable,
                preserveViewport: true
            });
            var waypts = [];
            for (var i = 0; i < waypoints.length; i++) {
                waypts.push({
                    location: new google.maps.LatLng(waypoints[i].GISY, waypoints[i].GISX),
                    stopover: (waypoints[i].hasOwnProperty('stopover') ? waypoints[i].stopover : true)
                });
            }
            var route_index = this._allRoutes.length;
            directionsDisplay.Line_Color = line_color;
            this.CalcRoute(route_index, directionsDisplay, start, end, waypts, callback);
            this._allRoutes.push(directionsDisplay);
            return route_index;
        }
        EupMap.prototype.DrawDisplayRoute = function (directionsDisplay, start, end, waypts) {
            var that = this;
            this.directionsService.route({
                origin: start,
                destination: end,
                waypoints: waypts,
                travelMode: 'WALKING',
                avoidTolls: true
            }, function (response, status) {
                if (status === 'OK') {
                    directionsDisplay.setDirections(response);
                } else if (status == google.maps.DirectionsStatus.OVER_QUERY_LIMIT) {
                    var retry = function () {
                        that.DrawDisplayRoute(directionsDisplay, start, end, waypts);
                    }
                    setTimeout(retry, 10);
                } else {
                    console.log(status);
                }
            });
        }
        //Function Name: 
        //Modify Date:
        //Author: ThiemNN
        EupMap.prototype.DisplayRoute = function (start_gisx, start_gisy, end_gisx, end_gisy, route_color, draggable, opacity, waypoints) {
            var start = new google.maps.LatLng(start_gisy, start_gisx);
            var end = new google.maps.LatLng(end_gisy, end_gisx);
            var route_color = route_color || "#FF000";
            var draggable = draggable ? true : false;

            var directionsDisplay = new google.maps.DirectionsRenderer({
                polylineOptions: {
                    strokeColor: route_color,
                    strokeOpacity: opacity,
                    strokeWeight: 5,
                    zIndex: 1
                },
                suppressMarkers: true,
                map: this._map,
                draggable: draggable,
                preserveViewport: true
            });
            var waypts = [];
            for (var i = 0; i < waypoints.length; i++) {
                waypts.push({
                    location: new google.maps.LatLng(waypoints[i].lat(), waypoints[i].lng()),
                    stopover: true
                });
            }
            var route_index = this._allRoutes.length;
            this._allRoutes.push(directionsDisplay);
            this.DrawDisplayRoute(directionsDisplay, start, end, waypts);
            return route_index;
        }

        //Function Name: 
        //Modify Date:
        //Author: ThiemNN
        EupMap.prototype.ClearRoute = function () {
            for (var i = this._allRoutes.length - 1; i >= 0; i--) {
                this._allRoutes[i].setMap(null);
                this._allRoutes.splice(i, 1);
            }
        };
        EupMap.prototype.ClearLastRoute = function () {
            this._allRoutes[this._allRoutes.length - 1].setMap(null);
            this._allRoutes.splice(this._allRoutes.length - 1, 1);
        };
        //Function Name: 
        //Modify Date:
        //Author: ThiemNN
        EupMap.prototype.ClearMarkers = function (listMarkers) {
            for (var i = listMarkers.length - 1; i >= 0; i--) {
                this._allMarkers[listMarkers[i]].setVisible(false);
            }
        };

        EupMap.prototype.ClearStopMarkers = function () {
            for (var i = 0; i < this._stopMarkers.length; i++)
                this._stopMarkers[i].setMap(null);
            this._stopMarkers.splice(0, this._stopMarkers.length);
        };

        EupMap.prototype.RemovePointMarker = function (index) {
            if (typeof (this._pointMarkers[index]) == "undefined")
                return;

            this._pointMarkers[index].setMap(null);
            this._pointMarkers.splice(index, 1);
        };

        EupMap.prototype.ClearPointMarkers = function () {
            for (var i = 0; i < this._pointMarkers.length; i++)
                this._pointMarkers[i].setMap(null);
            this._pointMarkers.splice(0, this._pointMarkers.length);
        };

        EupMap.prototype.ClearLastPointMarkers = function () {
            this._pointMarkers[this._pointMarkers.length - 1].setMap(null);
            this._pointMarkers.splice(this._pointMarkers.length - 1, 1);
        };

        //**方法：清除路徑規劃**
        EupMap.prototype.RemoveRoute = function (route_index) {
            //this._allRoutes[route_index].setDirections({ routes: [] });
            if (this._allRoutes[route_index].hasOwnProperty('RouteLine'))
                this._allRoutes[route_index].RouteLine.setMap(null);
            this._allRoutes[route_index].setMap(null);
            this._allRoutes.splice(route_index, 1);
        };

        //**方法：清除所有路徑規劃**
        EupMap.prototype.RemoveAllRoute = function () {
            for (var i = 0; i < this._allRoutes.length; i++) {
                if (this._allRoutes[i].hasOwnProperty('RouteLine'))
                    this._allRoutes[i].RouteLine.setMap(null);
                this._allRoutes[i].setMap(null);
            }
            this._allRoutes.length = 0;
        };

        //**方法：增加規劃路徑站點**
        EupMap.prototype.AddWayPointOnRoute = function (route_index, gisx, gisy) {
            var request = this._allRoutes[route_index].directions.request;
            this.CalcRoute(route_index, this._allRoutes[route_index], request.origin, request.destination, [{
                location: new google.maps.LatLng(gisy, gisx)
            }]);
        }

        //**方法：清除規劃路徑所有中繼點**
        EupMap.prototype.ClearAllWayPointOnRoute = function (route_index) {
            var request = this._allRoutes[route_index].directions.request;
            this.CalcRoute(route_index, this._allRoutes[route_index], request.origin, request.destination, []);
        }

        //**方法：畫路徑規劃上的線**
        EupMap.prototype.DrawPolyLineOnRoute = function (response) {
            var directions = response.routes[0].legs;
            var points = [];
            for (var z = 0; z < directions.length; z++) {
                var direction = directions[z];
                for (var i = 0; i < direction.steps.length; i++) {
                    for (var j = 0; j < direction.steps[i].lat_lngs.length; j++) {
                        points.push(direction.steps[i].lat_lngs[j]);
                    }
                }
            }
            var routLine = new google.maps.Polyline({
                path: points,
                strokeColor: response.Line_Color,
                strokeOpacity: 1,
                strokeWeight: 5
            });
            routLine.setMap(this._map);
            return routLine;
        }

        //**方法：設定路徑規劃 change 事件**
        EupMap.prototype.AddChangedEventOnRoute = function (route_index, callback) {
            var that = this;
            var directionsDisplay = this._allRoutes[route_index];
            directionsDisplay.addListener('directions_changed', function () {
                try {
                    var direction = directionsDisplay.getDirections();
                    direction.Line_Color = directionsDisplay.Line_Color;
                    var routLine = that.DrawPolyLineOnRoute(direction);
                    var rightclickevent = undefined;
                    var dbclickevent = undefined;
                    if (directionsDisplay.hasOwnProperty('RouteLine')) {
                        directionsDisplay.RouteLine.setMap(null);
                        if (directionsDisplay.RouteLine.hasOwnProperty('rightclickevent'))
                            rightclickevent = directionsDisplay.RouteLine.rightclickevent;
                        if (directionsDisplay.RouteLine.hasOwnProperty('dbclickevent'))
                            dbclickevent = directionsDisplay.RouteLine.dbclickevent;
                    }
                    directionsDisplay.RouteLine = routLine;
                    //check reBind RightClickEvent
                    if (rightclickevent)
                        that.AddRightClickEventOnRoute(route_index, rightclickevent);
                    //check reBind DbClickEvent
                    if (dbclickevent)
                        that.AddRightClickEventOnRoute(route_index, dbclickevent);
                    //check direction over waypoint limit
                    if (direction.request.waypoints.length >= that.DIRECTION_WAYPOINTS_LIMIT) {
                        that.CalcRoute(route_index, that._allRoutes[route_index], direction.request.origin, direction.request.destination, []);
                    } else {
                        callback(route_index, direction);
                    }
                } catch (e) {
                    console.log(e.message)
                };
            });
        }
        //Function Name: 
        //Modify Date:
        //Author: ThiemNN
        EupMap.prototype.AddDragEndEventOnRoute = function (route_index, callback) {
            var directionsDisplay = this._allRoutes[route_index];
            directionsDisplay.addListener('directions_changed', function () {
                var direction = directionsDisplay.getDirections();
                if (direction.b) {
                    callback(route_index, direction);
                }
            });
        }

        // ****方法:切換路徑規劃模式 *****
        EupMap.prototype.ChangeRouteType = function (route_index, routeType) {
            var route_line = this._allRoutes[route_index].RouteLine;
            if (routeType == 1) {
                this._allRoutes[route_index].setOptions({
                    draggable: false
                });
                route_line.setOptions({
                    zIndex: 2
                });
            } else if (this._allRoutes[route_index].draggable) {
                this._allRoutes[route_index].setOptions({
                    draggable: true
                });
                route_line.setOptions({
                    zIndex: 0
                });
            }
        }

        // ****方法:取得路徑規劃模式 *****
        EupMap.prototype.GetRouteType = function (route_index) {
            var route_line = this._allRoutes[route_index].RouteLine;
            return route_line.zIndex == 2 ? 1 : 0;
        }

        // ****方法:加入路徑規劃 rightclick 事件 *****
        EupMap.prototype.AddRightClickEventOnRoute = function (route_index, callBack) {
            var route_line = this._allRoutes[route_index].RouteLine;
            var draggable = this._allRoutes[route_index].draggable;
            route_line.setOptions({
                zIndex: (draggable ? 0 : 2)
            });
            route_line.rightclickevent = callBack;
            google.maps.event.addListener(route_line, 'rightclick', function (event) {
                callBack(event);
            });
        };

        // ****方法:加入路徑規劃 dbclick 事件 *****
        EupMap.prototype.AddDbClickEventOnRoute = function (route_index, callBack) {
            var route_line = this._allRoutes[route_index].RouteLine;
            var draggable = this._allRoutes[route_index].draggable;
            route_line.setOptions({
                zIndex: (draggable ? 0 : 2)
            });
            route_line.dbclickevent = callBack;
            google.maps.event.addListener(route_line, 'dbclick', function (event) {
                callBack(event);
            });
        };

        // ****方法:尋找marker index *****
        EupMap.prototype._FindMarkerIndex = function (marker) {
            var len = this._allMarkers.length || 0;
            for (var i = 0; i < len; i++)
                if (this._allMarkers[i] == marker) return i;
            return -1;
        };
        // ****方法:尋找line index *****
        EupMap.prototype._FindLineIndex = function (line) {
            var len = this._allLines.length || 0;
            for (var i = 0; i < len; i++)
                if (this._allLines[i][0] == line) return i;
            return -1;
        };
        // ****方法:尋找polygon index *****
        EupMap.prototype._FindPolygonIndex = function (polygon) {
            var len = this._allPolygon.length || 0;
            for (var i = 0; i < len; i++) {
                if (this._allPolygon[i][0] == polygon) return i;
            }
            return -1;
        };
        // ****方法:尋找具有箭頭的折線polygon index *****
        EupMap.prototype._FindPolylineWithArrowIndex = function (polygon) {
            var len = this._polylineWithArrow.length || 0;
            for (var i = 0; i < len; i++) {
                if (this._polylineWithArrow[i][0] == polygon) return i;
            }
            return -1;
        };
        // ****方法:尋找circle index *****
        EupMap.prototype._FindCircleIndex = function (circle) {
            var len = this._allCircle.length || 0;
            for (var i = 0; i < len; i++)
                if (this._allCircle[i][0] == circle) return i;
            return -1;
        };
        // ****方法:設定地圖Zoom極大極小值 *****
        EupMap.prototype.SetMapZommLimit = function (_minZoom, _maxZoom) {
            var opt = {};
            if (_minZoom) opt.minZoom = _minZoom;
            if (_maxZoom) opt.maxZoom = _maxZoom;
            this._map.setOptions(opt);
        };
        // ****方法:設定地圖Style *****
        EupMap.prototype.SetMapShowStyleById = function (id) {
            var styles = new EupMapStyleType().GetEupMapStyleById(id);
            this._map.setOptions({
                styles: styles
            });
        };

        // **** 方法:設定Map類型 *****
        EupMap.prototype.SetMapType = function (typeid) {
            if (typeid == 0)
                this._map.setMapTypeId(google.maps.MapTypeId.ROADMAP);
            else if (typeid == 1)
                this._map.setMapTypeId(google.maps.MapTypeId.SATELLITE);
            else if (typeid == 2)
                this._map.setMapTypeId(google.maps.MapTypeId.HYBRID);
            else
                this._map.setMapTypeId(google.maps.MapTypeId.TERRAIN);
        };

        // **** 方法:取得Map類型 *****
        EupMap.prototype.GetMapType = function () {
            var showStreetViewControl = this._map.getMapTypeId();
            return showStreetViewControl;
        };

        // ****方法:設定Map all 屬性 *****
        EupMap.prototype.SetMapOptions = function (disableDefaultUI, disableDoubleClickZoom, draggable, keyboardShortcuts, mapTypeControl, navigationControl, scaleControl, scrollwheel, streetViewControl, zoom, overviewMapControl, panControl, rotateControl, zoomControl, zoomControlPosition, streetViewControlPosition, mapTypeControlPosition, fullscreenControl) {
            var opt = {};
            if (disableDefaultUI != undefined) opt.disableDefaultUI = disableDefaultUI;
            if (disableDoubleClickZoom != undefined) opt.disableDoubleClickZoom = disableDoubleClickZoom;
            if (draggable != undefined) opt.draggable = draggable;
            if (keyboardShortcuts != undefined) opt.keyboardShortcuts = keyboardShortcuts;
            if (navigationControl != undefined) opt.navigationControl = navigationControl;
            if (scaleControl != undefined) opt.scaleControl = scaleControl;
            if (scrollwheel != undefined) opt.scrollwheel = scrollwheel;
            if (streetViewControl != undefined) opt.streetViewControl = streetViewControl;
            if (zoom != undefined) opt.zoom = zoom;
            if (overviewMapControl != undefined) opt.overviewMapControl = overviewMapControl;
            if (panControl != undefined) opt.panControl = panControl;
            if (rotateControl != undefined) opt.rotateControl = rotateControl;
            if (zoomControl != undefined) opt.zoomControl = zoomControl;
            if (zoomControlPosition != undefined) opt.zoomControlOptions = {
                position: zoomControlPosition
            };
            if (streetViewControlPosition != undefined) opt.streetViewControlOptions = {
                position: streetViewControlPosition
            };
            if (fullscreenControl != undefined) opt.fullscreenControl = fullscreenControl;
            if (mapTypeControl != undefined) {
                opt.mapTypeControl = mapTypeControl;
                opt.mapTypeControlOptions = {
                    style: google.maps.MapTypeControlStyle.HORIZONTAL_BAR,
                    position: mapTypeControlPosition || google.maps.ControlPosition.LEFT_BOTTOM
                };
            }

            this._map.setOptions(opt);
        };

        // ****方法:設定顯示路況圖層是或否 *****
        EupMap.prototype.SetTrafficLayerVisible = function (isEnabled) {
            this._trafficLayer.setMap(isEnabled ? this._map : null);
        };
        // ****方法:取得目前路況圖層是否顯示 *****
        EupMap.prototype.GetTrafficLayerVisible = function () {
            return this._trafficLayer.getMap() == this._map ? true : false;
        };
        // ****方法:取得地圖 Zoom *****
        EupMap.prototype.GetMapZoom = function () {
            return this._map.getZoom();
        };
        // ****方法:取得指定xy對應目前可視地圖上左上角(0,0)的銀幕座標,回傳字串 *****
        EupMap.prototype.GetScreenPoint_FromMapLeftTop = function (gisX, gisY) {
            var pixelOffset = this._GetScreenPoint_FromMapLeftTop(gisX, gisY);
            return pixelOffset.x + "," + pixelOffset.y;
        };
        // ****方法:取得指定xy對應目前可視地圖上左上角(0,0)的銀幕座標,回傳物件 *****
        EupMap.prototype._GetScreenPoint_FromMapLeftTop = function (gisX, gisY) {
            var map = this._map;
            var pnt = new google.maps.LatLng(gisY, gisX);
            var scale = Math.pow(2, map.getZoom());
            var b = map.getBounds();
            var nw = new google.maps.LatLng(
                b.getNorthEast().lat(),
                b.getSouthWest().lng()
            );
            var pj = map.getProjection();
            var worldCoordinateNW = pj.fromLatLngToPoint(nw);
            var worldCoordinate = pj.fromLatLngToPoint(pnt);
            var pixelOffset = new google.maps.Point(
                Math.floor((worldCoordinate.x - worldCoordinateNW.x) * scale),
                Math.floor((worldCoordinate.y - worldCoordinateNW.y) * scale)
            );
            return pixelOffset;
        };
        // ****方法:取得指定pixel xy 目前經緯度,回傳物件 *****
        EupMap.prototype._GetGisXYFromViewPixelXY = function (pixelX, pixelY) {
            var map = this._map;
            var scale = Math.pow(2, map.getZoom());
            var b = map.getBounds();
            var nw = new google.maps.LatLng(
                b.getNorthEast().lat(),
                b.getSouthWest().lng()
            );
            var worldCoordinateNW = map.getProjection().fromLatLngToPoint(nw);
            var newX = ((scale * worldCoordinateNW.x) + pixelX) / scale;
            var newY = ((scale * worldCoordinateNW.y) + pixelY) / scale;
            var gisXY = map.getProjection().fromPointToLatLng(new google.maps.Point(newX, newY));
            return gisXY;
        };
        // ****方法:取得地圖中心點Gis X *****
        EupMap.prototype.GetCenterGisX = function () {
            return this._map.getCenter().lng()
        };
        // ****方法:取得地圖中心點Gis Y *****
        EupMap.prototype.GetCenterGisY = function () {
            return this._map.getCenter().lat()
        };
        // ****方法:取得地圖中心點Gis XY *****
        EupMap.prototype.GetCenterGisXY = function () {
            var latlng = this._map.getCenter();
            return latlng.lng() + "," + latlng.lat();
        };
        // ****方法:移動地圖使中心點位於指定XY *****
        EupMap.prototype.MoveCenterTo = function (gisX, gisY, EnablePan) {

            var ddddd = new google.maps.LatLng(gisY, gisX);
            if (EnablePan) return this._map.panTo(new google.maps.LatLng(gisY, gisX));
            else return this._map.setCenter(new google.maps.LatLng(gisY, gisX));
        };
        // ****方法:移動地圖使中心點位於相對XY *****
        EupMap.prototype.panByTo = function (X, Y) {
            this._map.panBy(X, Y);
        };
        // ****方法:地址轉換Gis座標 *****
        EupMap.prototype.AddressToLatLng = function (addr, callback) {
            var geocoder = new google.maps.Geocoder();
            var codedAddress;
            geocoder.geocode({
                "address": addr
            }, function (results, status) {
                if (status == google.maps.GeocoderStatus.OK) {
                    var Gis_X = results[0].geometry.location.lng();
                    var Gis_Y = results[0].geometry.location.lat();
                    callback(Gis_X, Gis_Y);
                } else {
                    alert('解析失敗!回傳狀態為：' + status);
                }
            });
        };
        //David
        EupMap.prototype.LatLngToAddress = function (lat, lng, callback) {
            var geocoder = new google.maps.Geocoder();
            var latlng = { lat: lat, lng: lng };
            geocoder.geocode({ 'location': latlng }, function (results, status) {
                if (status === 'OK') {
                    if (results[0]) {
                        callback(results);
                    } else {
                        // window.alert('No results found');
                    }
                } else {
                    // window.alert('Geocoder failed due to: ' + status);
                }
            });
        };
        // End David
        EupMap.prototype.rad = function (x) {
            return x * Math.PI / 180;
        };
        // ****方法:兩點經緯度計算距離 *****
        EupMap.prototype.GetDistance = function (gisX1, gisY1, gisX2, gisY2) {
            var R = 6378137; // Earth’s mean radius in meter
            var dLat = EupMap.prototype.rad(gisY2 - gisY1);
            var dLong = EupMap.prototype.rad(gisX2 - gisX1);
            var a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(EupMap.prototype.rad(gisY1)) * Math.cos(EupMap.prototype.rad(gisY2)) * Math.sin(dLong / 2) * Math.sin(dLong / 2);
            var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            var distance = R * c;
            return distance; // returns the distance in meter
        };
        // ****方法:取得目前地圖邊界:回傳x0,x1,y0,y1陣列 *****
        EupMap.prototype.GetMapBoundsArry = function () {
            var b = this._map.getBounds();
            return [b.getSouthWest().lng(), b.getNorthEast().lng(), b.getSouthWest().lat(), b.getNorthEast().lat()];
        };
        // ****方法:取得目前地圖邊界 回傳x0,x1,y0,y1字串 *****
        EupMap.prototype.GetMapBounds = function () {
            var b = this._map.getBounds();
            return b.getSouthWest().lng() + "," + b.getNorthEast().lng() + "," + b.getSouthWest().lat() + "," + b.getNorthEast().lat();
        };
        // ****方法:取得是否目前地圖邊界內 *****
        EupMap.prototype.IsInsideMapBounds = function (gisX, gisY) {
            var r = this.GetMapBoundsArry();
            return (gisX >= r[0] && gisX <= r[1] && gisY >= r[2] && gisY <= r[3]);
        };
        // ****方法:加入tilesloaded 事件 *****
        EupMap.prototype.AddTilesloadedEventOnMap = function (callBack) {
            google.maps.event.addListener(this._map, 'tilesloaded', function () {
                callBack();
            });
        };
        // ****方法:加入 map idle 事件 *****
        EupMap.prototype.AddIdleEventOnMap = function (callBack) {
            google.maps.event.addListener(this._map, 'idle', function () {
                callBack();
            });
        };
        // ****方法:加入 街景之 visible_changed 事件 *****
        EupMap.prototype.AddVisibleChangedEventOnStreetViewPanorama = function (callBack) {
            var me = this;
            google.maps.event.addListener(this._map.getStreetView(), 'visible_changed', function () {
                callBack(me._map.getStreetView().getVisible());
            });
        };
        // ****方法:加入Map rightclick 事件 *****
        EupMap.prototype.AddRightClickEventOnMap = function (callBack) {
            var me = this;
            google.maps.event.addListener(this._map, 'rightclick', function (event) {
                var point = me._GetScreenPoint_FromMapLeftTop(event.latLng.lng(), event.latLng.lat());
                callBack(event.latLng.lng(), event.latLng.lat(), point.x, point.y);
            });
        };
        // ****方法:加入Map click 事件 *****
        EupMap.prototype.AddClickEventOnMap = function (callBack) {
            var me = this;
            google.maps.event.addListener(this._map, 'click', function (event) {
                var point = me._GetScreenPoint_FromMapLeftTop(event.latLng.lng(), event.latLng.lat());
                callBack(event.latLng.lng(), event.latLng.lat(), point.x, point.y);
            });
        };

        EupMap.prototype.PolygonPrevious = function () {
            if (this._nowPolygonPoints.length <= 1)
                return;
            this._nowPolygonPoints.splice(this._nowPolygonPoints.length - 1, 1);
            this.RemovePolygon(this._allPolygon.length - 1);
            var index = this.AddOnePolygon(this._nowPolygonPoints[this._nowPolygonPoints.length - 1], 'black', 0.8, 3, 'black', 0.35, 10);
            this.SetPolygonEditable(index, true);
            this.PolygonChangeEvent(this._allPolygon[index]);
        }

        EupMap.prototype.CirclePrevious = function () {
            if (this._nowCirclePoints.length <= 1)
                return;
            this._nowCirclePoints.splice(this._nowCirclePoints.length - 1, 1);
            this.RemoveCircle(this._allCircle.length - 1);
            var CirclePreVal = this._nowCirclePoints[this._nowCirclePoints.length - 1];
            var index = this.AddOneCircle(CirclePreVal.Center.split(",")[0], CirclePreVal.Center.split(",")[1], true, 'black', 0.35, CirclePreVal.Radius * 2, 'black', 0.8, 3, 10);
            this.SetCircleEditable(index, true);
            this.CircleChangeEvent(index);
        }

        // 多邊形polygon紀錄修改路徑事件
        EupMap.prototype.PolygonChangeEvent = function (polygon) {
            var that = this;
            google.maps.event.addListener(polygon[0].getPath(), 'set_at', function () {
                var tempAry = that.GetPolygonPathByObject(polygon);
                that._nowPolygonPoints.push(tempAry);
            });
            google.maps.event.addListener(polygon[0].getPath(), 'insert_at', function () {
                var tempAry = that.GetPolygonPathByObject(polygon);
                that._nowPolygonPoints.push(tempAry);
            });
        };

        // 圓形circle紀錄修改路徑事件
        EupMap.prototype.CircleChangeEvent = function (circle) {
            var that = this;
            google.maps.event.addListener(that._allCircle[circle][0], 'radius_changed', function () {
                var Radius = that.GetCircleRadius(circle);
                var Center = that.GetCircleCenter(circle);
                that._nowCirclePoints.push({ Radius: Radius, Center: Center });
            });
            google.maps.event.addListener(that._allCircle[circle][0], 'center_changed', function () {
                var Radius = that.GetCircleRadius(circle);
                var Center = that.GetCircleCenter(circle);
                that._nowCirclePoints.push({ Radius: Radius, Center: Center });
            });
        };

        // ****方法:加入Map dblclick 事件 *****
        EupMap.prototype.AddDbClickEventOnMap = function (callBack) {
            var me = this;
            google.maps.event.addListener(this._map, 'dblclick', function (event) {
                var point = me._GetScreenPoint_FromMapLeftTop(event.latLng.lng(), event.latLng.lat());
                callBack(event.latLng.lng(), event.latLng.lat(), point.x, point.y);
            });
        };
        // ****方法:加入Map zoom_changed 事件 *****
        EupMap.prototype.AddZoomChangedOnMap = function (callBack) {
            var currentMap = this._map;
            google.maps.event.addListener(currentMap, 'zoom_changed', function () {
                callBack(currentMap.getZoom());
            });
        };
        // ****方法:加入Map bounds_changed 事件 *****
        EupMap.prototype.AddBoundsChangedOnMap = function (callBack) {
            var _this = this; //.

            google.maps.event.addListener(this._map, 'bounds_changed', function () {
                callBack();

                //移除定位資訊//.
                _this.HideLocateMarkerInfo();
            });
        };
        // ****方法:加入Map Resize 事件 *****
        EupMap.prototype.AddResizeEventOnMap = function (callBack) {
            google.maps.event.addListener(this._map, 'resize', function () {
                callBack();
            });
        };
        // ****方法:取得目前Marker總數量 *****
        EupMap.prototype.GetAllMarkersLenth = function () {
            return this._allMarkers.length;
        };
        // ****方法:加入Marker,回傳:加入後的index *****
        EupMap.prototype.AddMarker = function (gisX, gisY, img, title, zIndex, anchorX, anchorY) {
            var options = {
                map: this._map,
                position: new google.maps.LatLng(gisY, gisX),
                icon: {
                    url: 'img/Default.png'
                }
            };
            if (title != undefined) options.title = title;
            if (zIndex != undefined) options.zIndex = zIndex;
            if (img != undefined) options.icon.url = img;
            if (anchorX != undefined && anchorY != undefined)
                options.icon.anchor = new google.maps.Point(anchorX, anchorY);
            var marker = new google.maps.Marker(options);
            return this._allMarkers.push(marker) - 1;
        };
        //Function Name: 
        //Modify Date:
        //Author: ThiemNN

        EupMap.prototype._FindStopMarkerIndex = function (marker) {
            var len = this._stopMarkers.length || 0;
            for (var i = 0; i < len; i++)
                if (this._stopMarkers[i] == marker) return i;
            return -1;
        };

        EupMap.prototype._FindPointMarkerIndex = function (marker) {
            var len = this._pointMarkers.length || 0;
            for (var i = 0; i < len; i++)
                if (this._pointMarkers[i] == marker) return i;
            return -1;
        };
        EupMap.prototype.AddMoveableMarker = function (gisX, gisY, img, title, zIndex, anchorX, anchorY) {
            var options = {
                map: this._map,
                draggable: true,
                position: new google.maps.LatLng(gisY, gisX),
                icon: {
                    url: 'img/Default.png'
                }
            };
            if (title != undefined) options.title = title;
            if (zIndex != undefined) options.zIndex = zIndex;
            if (img != undefined) options.icon.url = img;
            if (anchorX != undefined && anchorY != undefined)
                options.icon.anchor = new google.maps.Point(anchorX, anchorY);
            var marker = new google.maps.Marker(options);
            return this._allMarkers.push(marker) - 1;
        };
        //Function Name: 
        //Modify Date:
        //Author: ThiemNN
        EupMap.prototype.AddGarbageTruckMarker = function (gisX, gisY, img, title, draggable, opacity, zIndex, anchorX, anchorY, index) {
            var options = {
                map: this._map,
                draggable: draggable,
                // label: {text: index.toString(), color: 'black'},
                position: new google.maps.LatLng(gisY, gisX),
                icon: {
                    url: 'img/Default.png',
                }
            };
            if (title != undefined) options.title = title;
            if (zIndex != undefined) options.zIndex = zIndex;
            if (img != undefined) options.icon.url = img;
            if (anchorX != undefined && anchorY != undefined)
                options.icon.anchor = new google.maps.Point(anchorX, anchorY);
            var marker = new google.maps.Marker(options);
            marker.setOpacity(opacity);
            return this._pointMarkers.push(marker) - 1;
        };
        EupMap.prototype.ChangeGarbageTruckMarkerIcon = function (markerIndex, gisX, gisY, Icon, title, zIndex, anchorX, anchorY) {
            var marker = this._pointMarkers[markerIndex];
            marker.setIcon(Icon);
            /*var options = {
                map: this._map,
                position: new google.maps.LatLng(gisY, gisX),
                icon: Icon
            };
            if (title != undefined) options.title = title;
            if (zIndex != undefined) options.zIndex = zIndex;
            if (anchorX != undefined && anchorY != undefined)
                options.icon.anchor = new google.maps.Point(anchorX, anchorY);
            var marker = new google.maps.Marker(options);
            this._pointMarkers[markerIndex].setMap(null);
            // this._pointMarkers.splice(markerIndex, 1 , marker);
            this._pointMarkers[markerIndex] = marker;*/
        };
        // ****方法:更改Label 文字****
        EupMap.prototype.SetGarbageTruckMarkerText = function (index) {
            for (var i = 0; i < this._pointMarkers.length; i++) {
                var label = {
                    text: (i == index) ? index + 1 + "" : " ",
                    fontSize: "16px",
                    color: "red",
                }
                this._pointMarkers[i].setLabel(label);
            }
        };
        //Function Name: 
        //Modify Date:
        //Author: ThiemNN
        EupMap.prototype.AddGarbageMarker = function (gisX, gisY, img, index, text_color, draggable, opacity, zIndex, anchorX, anchorY) {
            var options = {
                map: this._map,
                draggable: draggable,
                label: { text: index.toString(), color: text_color },
                position: new google.maps.LatLng(gisY, gisX),
                icon: {
                    url: 'img/Default.png'
                }
            };
            if (zIndex != undefined) options.zIndex = zIndex;
            if (img != undefined) options.icon.url = img;
            if (anchorX != undefined && anchorY != undefined)
                options.icon.anchor = new google.maps.Point(anchorX, anchorY);
            var marker = new google.maps.Marker(options);
            marker.setOpacity(opacity);
            //return this._allMarkers.push(marker) - 1;
            return this._stopMarkers.push(marker) - 1;
        };

        // Harvey - new marker label
        EupMap._OverlayViews = [];
        EupMap.prototype.RemoveAllMarkerLabels = function () {
            if (EupMap._OverlayViews.length > 0) {
                EupMap._OverlayViews.forEach(function (label) {
                    label.setMap(null);
                });
                EupMap._OverlayViews = [];
            }
        }
        EupMap.prototype.SetMarkerLabel = function (markerIndex, label, color, fontSize, fontWeight) {
            var mk = this._allMarkers[markerIndex];
            var obj_label = new Label({
                map: this._map,
                text: label,
                color: color
            });
            EupMap._OverlayViews[markerIndex] = obj_label;
            obj_label.bindTo('position', mk, 'position');
        }
        EupMap.prototype.UpdateMarkerLabel = function (markerIndex, label, color, fontSize, fontWeight) {
            EupMap._OverlayViews[markerIndex].text = label;
            EupMap._OverlayViews[markerIndex].span_.style.color = color;
            var obj_label = EupMap._OverlayViews[markerIndex];
            var mk = this._pointMarkers[markerIndex];
            obj_label.bindTo('position', mk, 'position');
        }
        // End Harvey

        // ****方法:加入Marker,回傳:加入後的index *****
        EupMap.prototype.AddMarker_Icon = function (gisX, gisY, Icon, title, zIndex, anchorX, anchorY) {
            var options = {
                map: this._map,
                position: new google.maps.LatLng(gisY, gisX),
                icon: Icon
            };
            if (title != undefined) options.title = title;
            if (zIndex != undefined) options.zIndex = zIndex;
            if (anchorX != undefined && anchorY != undefined)
                options.icon.anchor = new google.maps.Point(anchorX, anchorY);
            var marker = new google.maps.Marker(options);
            return this._allMarkers.push(marker) - 1;
        };
        // ****方法:修改Marker Icon的顏色 Edit By Ian 2017/03/14*****
        EupMap.prototype.changeMarkerIcon = function (markerIndex, gisX, gisY, Icon, title, zIndex, anchorX, anchorY) {
            var options = {
                map: this._map,
                position: new google.maps.LatLng(gisY, gisX),
                icon: Icon
            };
            if (title != undefined) options.title = title;
            if (zIndex != undefined) options.zIndex = zIndex;
            if (anchorX != undefined && anchorY != undefined)
                options.icon.anchor = new google.maps.Point(anchorX, anchorY);
            var marker = new google.maps.Marker(options);
            this._allMarkers[markerIndex].setMap(null);
            this._allMarkers.splice(markerIndex, 1, marker);
        };
        // ****方法:移除特定一Marker *****
        EupMap.prototype.RemoveMarker = function (index) {
            if (typeof (this._allMarkers[index]) == "undefined")
                return;

            this._allMarkers[index].setMap(null);
            this._allMarkers.splice(index, 1);
        };

        // ****方法:移除全部Marker *****
        EupMap.prototype.RemoveAllMarkers = function () {
            for (var i = 0; i < this._allMarkers.length; i++)
                this._allMarkers[i].setMap(null);
            this._allMarkers.splice(0, this._allMarkers.length);
        }; // ****方法:加入DistanceMarker,回傳:加入後的index *****
        EupMap.prototype.AddDistanceMarker = function (gisX, gisY, img, title, zIndex, anchorX, anchorY) {
            var options = {
                map: this._map,
                position: new google.maps.LatLng(gisY, gisX),
                icon: {
                    url: 'img/Default.png'
                }
            };
            if (title != undefined) options.title = title;
            if (zIndex != undefined) options.zIndex = zIndex;
            if (img != undefined) options.icon.url = img;
            if (anchorX != undefined && anchorY != undefined)
                options.icon.anchor = new google.maps.Point(anchorX, anchorY);
            var marker = new google.maps.Marker(options);
            return this._distanceMarkers.push(marker) - 1;
        };
        // ****方法:移除特定一DistanceMarker *****
        EupMap.prototype.RemoveDistanceMarker = function (index) {
            this._distanceMarkers[index].setMap(null);
            this._distanceMarkers.splice(index, 1);
        };
        // ****方法:移除最後一DistanceMarker *****
        EupMap.prototype.RemoveDistancLasteMarker = function () {
            this._distanceMarkers[this._distanceMarkers.length - 1].setMap(null);
            this._distanceMarkers.splice(this._distanceMarkers.length - 1, 1);
        };
        // ****方法:移除全部DistanceMarker *****
        EupMap.prototype.RemoveAllDistanceMarkers = function () {
            for (var i = 0; i < this._distanceMarkers.length; i++)
                this._distanceMarkers[i].setMap(null);
            this._distanceMarkers.splice(0, this._distanceMarkers.length);
        };
        // ****方法:取得DistanceMarker 數量
        EupMap.prototype.GetAllDistanceMarkerssLenth = function () {
            return this._distanceMarkers.length;
        };
        // ****方法:加入DeleteMarker,回傳:加入後的index *****
        EupMap.prototype.AddDeleteMarker = function (gisX, gisY, img, title, zIndex, anchorX, anchorY) {
            var options = {
                map: this._map,
                position: new google.maps.LatLng(gisY, gisX),
                icon: {
                    url: 'img/Default.png'
                }
            };
            if (title != undefined) options.title = title;
            if (zIndex != undefined) options.zIndex = zIndex;
            if (img != undefined) options.icon.url = img;
            if (anchorX != undefined && anchorY != undefined)
                options.icon.anchor = new google.maps.Point(anchorX, anchorY);
            var marker = new google.maps.Marker(options);
            return this._deleteMarkers.push(marker) - 1;
        };
        // ****方法:移除最後一DeleteMarker *****
        EupMap.prototype.RemoveDeleteMarker = function () {
            this._deleteMarkers[this._deleteMarkers.length - 1].setMap(null);
            this._deleteMarkers.splice(this._deleteMarkers.length - 1, 1);
        };
        // ****方法:移除全部DeleteMarker *****
        EupMap.prototype.RemoveAllDeleteMarkers = function () {
            for (var i = 0; i < this._deleteMarkers.length; i++)
                this._deleteMarkers[i].setMap(null);
            this._deleteMarkers.splice(0, this._deleteMarkers.length);
        };
        // ****方法:加入UndoMarker,回傳:加入後的index *****
        EupMap.prototype.AddUndoMarker = function (gisX, gisY, img, title, zIndex, anchorX, anchorY) {
            var options = {
                map: this._map,
                position: new google.maps.LatLng(gisY, gisX),
                icon: {
                    url: 'img/Default.png'
                }
            };
            if (title != undefined) options.title = title;
            if (zIndex != undefined) options.zIndex = zIndex;
            if (img != undefined) options.icon.url = img;
            if (anchorX != undefined && anchorY != undefined)
                options.icon.anchor = new google.maps.Point(anchorX, anchorY);
            var marker = new google.maps.Marker(options);
            return this._undoMarkers.push(marker) - 1;
        };
        // ****方法:移除最後一UndoMarker *****
        EupMap.prototype.RemoveUndoMarker = function () {
            this._undoMarkers[this._undoMarkers.length - 1].setMap(null);
            this._undoMarkers.splice(this._undoMarkers.length - 1, 1);
        };
        // ****方法:移除全部UndoMarker *****
        EupMap.prototype.RemoveAllUndoMarkers = function () {
            for (var i = 0; i < this._undoMarkers.length; i++)
                this._undoMarkers[i].setMap(null);
            this._undoMarkers.splice(0, this._undoMarkers.length);
        };
        // ****方法:加入DeleteMarker click 事件 *****
        EupMap.prototype.AddClickEventOnDeleteMarker = function (deleteIndex, callBack) {
            var me = this;
            var currentMarker = this._deleteMarkers[deleteIndex];
            google.maps.event.addListener(currentMarker, 'click', function (event) {
                var cursor_gisXY = me._GetGisXYFromViewPixelXY(me._cursorX, me._cursorY); // ** for cursor **
                var point = me._GetScreenPoint_FromMapLeftTop(event.latLng.lng(), event.latLng.lat()); // ** for marker **
                var idx = me._FindDeleteMarkerIndex(currentMarker);
                if (idx >= 0) callBack(idx, event.latLng.lng(), event.latLng.lat(), point.x, point.y, cursor_gisXY.lng(), cursor_gisXY.lat(), me._cursorX, me._cursorY);
            });
        };
        // ****方法:尋找Deletemarker index *****
        EupMap.prototype._FindDeleteMarkerIndex = function (marker) {
            var len = this._deleteMarkers.length || 0;
            for (var i = 0; i < len; i++)
                if (this._deleteMarkers[i] == marker) return i;
            return -1;
        };
        // ****方法:加入UndoMarker click 事件 *****
        EupMap.prototype.AddClickEventOnUndoMarker = function (undoIndex, callBack) {
            var me = this;
            var currentMarker = this._undoMarkers[undoIndex];
            google.maps.event.addListener(currentMarker, 'click', function (event) {
                var cursor_gisXY = me._GetGisXYFromViewPixelXY(me._cursorX, me._cursorY); // ** for cursor **
                var point = me._GetScreenPoint_FromMapLeftTop(event.latLng.lng(), event.latLng.lat()); // ** for marker **
                var idx = me._FindUndoMarkerIndex(currentMarker);
                if (idx >= 0) callBack(idx, event.latLng.lng(), event.latLng.lat(), point.x, point.y, cursor_gisXY.lng(), cursor_gisXY.lat(), me._cursorX, me._cursorY);
            });
        };
        // ****方法:尋找Undomarker index *****
        EupMap.prototype._FindUndoMarkerIndex = function (marker) {
            var len = this._undoMarkers.length || 0;
            for (var i = 0; i < len; i++)
                if (this._undoMarkers[i] == marker) return i;
            return -1;
        };
        // ****方法:加入一Distance折線至地圖*******
        EupMap.prototype.AddDistanceLine = function (pointarry, geodesic, htmlColor, linestrokeopacity, lineWeight, zIndex) {
            var opt = {
                path: pointarry,
                geodesic: geodesic,
                strokeColor: htmlColor,
                strokeOpacity: linestrokeopacity,
                strokeWeight: lineWeight,
                zIndex: zIndex,
                map: this._map
            };
            var newLine = new google.maps.Polyline(opt);
            return this._distanceLines.push([newLine, opt]) - 1;
        };

        // ****方法:加入一具有箭頭的折線至地圖*******
        EupMap.prototype.AddPolylineWithArrow = function (pointarry, iconColor, linestrokeopacity, lineWeight, direction) {
            var iconArrow = {
                // path: google.maps.SymbolPath.FORWARD_CLOSED_ARROW,
                path: 'M0 -2 L-2 2 L0 1 L2 2 L 0 -2',
                fillColor: '#FFFFFF',
                fillOpacity: 0.8,
                strokeColor: '#000000',
                strokeWeight: 1,
                strokeOpacity: 0.8,
                scale: 3,
                rotation: direction
            };
            var iconCircle = {
                path: google.maps.SymbolPath.CIRCLE,
                fillColor: iconColor,
                fillOpacity: 0.8,
                strokeColor: '#000000',
                strokeWeight: 1,
                strokeOpacity: 0.8,
                scale: 9
            };
            var opt = {
                path: pointarry,
                strokeColor: 'rgba(0, 0, 0, 0.0) ',
                strokeOpacity: 1,
                strokeWeight: 17,
                zIndex: 9999,
                map: this._map,
                icons: [
                    {
                        icon: iconCircle,
                        offset: '0%'
                    }, {
                        icon: iconArrow,
                        offset: '0%'
                    }]
            };
            var newLine = new google.maps.Polyline(opt);
            return this._polylineWithArrow.push([newLine, opt]) - 1;
        };

        // ****方法:加入一具有箭頭的折線事件*******
        EupMap.prototype.AddPolylineWithArrowEvent = function (lineIndex, pointInfo) {
            var infoWindow = new google.maps.InfoWindow({ pixelOffset: new google.maps.Size(0, -10) });
            var _self = this;
            google.maps.event.addListener(_self._polylineWithArrow[lineIndex][0], 'mouseover', function (e) {
                infoWindow.setPosition(e.latLng);
                infoWindow.setContent(pointInfo);
                infoWindow.open(_self._map, _self._polylineWithArrow[lineIndex][0]);
            });
            google.maps.event.addListener(_self._polylineWithArrow[lineIndex][0], 'mouseout', function (e) {
                infoWindow.close();
            });
        };

        // ****方法:加入一具有箭頭的折線右鍵點擊事件*******
        EupMap.prototype.AddPolylineWithArrowRightClickEvent = function (lineIndex, callBack) {
            var me = this;
            var currentPolygon = this._polylineWithArrow[lineIndex][0];
            google.maps.event.addListener(currentPolygon, 'rightclick', function (event) {
                var point = me._GetScreenPoint_FromMapLeftTop(event.latLng.lng(), event.latLng.lat());
                var idx = me._FindPolylineWithArrowIndex(currentPolygon);
                if (idx >= 0) callBack(idx, event.latLng.lng(), event.latLng.lat(), point.x, point.y, (event.edge == undefined ? -1 : event.edge), (event.path == undefined ? -1 : event.path), (event.vertex == undefined ? -1 : event.vertex));
            });
        };

        // ****方法:更改折線箭頭的顏色*******
        EupMap.prototype.ChangePolylineWithArrow = function (lineIndex, iconColor, zIndex) {
            this._polylineWithArrow[lineIndex][1].icons[0].icon.fillColor = iconColor;
            this._polylineWithArrow[lineIndex][1].zIndex = zIndex;
            this._polylineWithArrow[lineIndex][0].setOptions(this._polylineWithArrow[lineIndex][1]);
        };

        // ****方法:刪除所有折線箭頭*******
        EupMap.prototype.ClearAllPolylineWithArrow = function () {
            for (var i = 0; i < this._polylineWithArrow.length; i++) {
                this._polylineWithArrow[i][0].setMap(null);
                this._polylineWithArrow[i][0] = null;
            }
            this._polylineWithArrow = new Array();
        };
        // ****方法:加入禁行路段線段*******
        EupMap.prototype.AddForbiddenRoute = function (pointarry, linecolor, linestrokeopacity, lineWeight, zIndex) {
            var opt = {
                path: pointarry,
                strokeColor: linecolor,
                strokeOpacity: linestrokeopacity,
                strokeWeight: lineWeight,
                zIndex: zIndex,
                map: this._map
            };
            var newLine = new google.maps.Polyline(opt);
            return this._forbiddenRoute.push(newLine) - 1;
        };
        // ****方法:刪除禁行路段線段*******
        EupMap.prototype.RemoveForbiddenRoute = function () {
            for (var i = 0; i < this._forbiddenRoute.length; i++)
                this._forbiddenRoute[i].setMap(null);
            this._forbiddenRoute = new Array();
        };
        // ****方法:移除最後一DistanceLine********
        EupMap.prototype.RemoveLastDistanceLine = function () {
            this._distanceLines[this._distanceLines.length - 1][0].setMap(null);
            this._distanceLines.splice(this._distanceLines.length - 1, 1);
        };
        // ****方法:移除全部DistanceLine********
        EupMap.prototype.RemoveAllDistanceLines = function () {
            for (var i = 0; i < this._distanceLines.length; i++)
                this._distanceLines[i][0].setMap(null);
            this._distanceLines.splice(0, this._distanceLines.length);
        };
        // *** 方法:取得Line 數量 *****
        EupMap.prototype.GetAllDistanceLinesLenth = function () {
            return this._distanceLines.length;
        };
        // *** 方法:移除地圖 click 事件 *****
        EupMap.prototype.removeClickEventOnMap = function () {
            google.maps.event.clearListeners(this._map, 'click');
        }
        // ****方法:加入DistanceMarker,回傳:加入後的index *****
        EupMap.prototype.AddDistanceMarker = function (gisX, gisY, img, title, zIndex, anchorX, anchorY) {
            var options = {
                map: this._map,
                position: new google.maps.LatLng(gisY, gisX),
                icon: {
                    url: 'img/Default.png'
                }
            };
            if (title != undefined) options.title = title;
            if (zIndex != undefined) options.zIndex = zIndex;
            if (img != undefined) options.icon.url = img;
            if (anchorX != undefined && anchorY != undefined)
                options.icon.anchor = new google.maps.Point(anchorX, anchorY);
            var marker = new google.maps.Marker(options);
            return this._distanceMarkers.push(marker) - 1;
        };
        // ****方法:移除特定一DistanceMarker *****
        EupMap.prototype.RemoveDistanceMarker = function (index) {
            this._distanceMarkers[index].setMap(null);
            this._distanceMarkers.splice(index, 1);
        };
        // ****方法:移除最後一DistanceMarker *****
        EupMap.prototype.RemoveDistancLasteMarker = function () {
            this._distanceMarkers[this._distanceMarkers.length - 1].setMap(null);
            this._distanceMarkers.splice(this._distanceMarkers.length - 1, 1);
        };
        // ****方法:移除全部DistanceMarker *****
        EupMap.prototype.RemoveAllDistanceMarkers = function () {
            for (var i = 0; i < this._distanceMarkers.length; i++)
                this._distanceMarkers[i].setMap(null);
            this._distanceMarkers.splice(0, this._distanceMarkers.length);
        };
        // ****方法:取得DistanceMarker 數量
        EupMap.prototype.GetAllDistanceMarkerssLenth = function () {
            return this._distanceMarkers.length;
        };
        // ****方法:加入DeleteMarker,回傳:加入後的index *****
        EupMap.prototype.AddDeleteMarker = function (gisX, gisY, img, title, zIndex, anchorX, anchorY) {
            var options = {
                map: this._map,
                position: new google.maps.LatLng(gisY, gisX),
                icon: {
                    url: 'img/Default.png'
                }
            };
            if (title != undefined) options.title = title;
            if (zIndex != undefined) options.zIndex = zIndex;
            if (img != undefined) options.icon.url = img;
            if (anchorX != undefined && anchorY != undefined)
                options.icon.anchor = new google.maps.Point(anchorX, anchorY);
            var marker = new google.maps.Marker(options);
            return this._deleteMarkers.push(marker) - 1;
        };
        // ****方法:移除最後一DeleteMarker *****
        EupMap.prototype.RemoveDeleteMarker = function () {
            this._deleteMarkers[this._deleteMarkers.length - 1].setMap(null);
            this._deleteMarkers.splice(this._deleteMarkers.length - 1, 1);
        };
        // ****方法:移除全部DeleteMarker *****
        EupMap.prototype.RemoveAllDeleteMarkers = function () {
            for (var i = 0; i < this._deleteMarkers.length; i++)
                this._deleteMarkers[i].setMap(null);
            this._deleteMarkers.splice(0, this._deleteMarkers.length);
        };
        // ****方法:加入UndoMarker,回傳:加入後的index *****
        EupMap.prototype.AddUndoMarker = function (gisX, gisY, img, title, zIndex, anchorX, anchorY) {
            var options = {
                map: this._map,
                position: new google.maps.LatLng(gisY, gisX),
                icon: {
                    url: 'img/Default.png'
                }
            };
            if (title != undefined) options.title = title;
            if (zIndex != undefined) options.zIndex = zIndex;
            if (img != undefined) options.icon.url = img;
            if (anchorX != undefined && anchorY != undefined)
                options.icon.anchor = new google.maps.Point(anchorX, anchorY);
            var marker = new google.maps.Marker(options);
            return this._undoMarkers.push(marker) - 1;
        };
        // ****方法:移除最後一UndoMarker *****
        EupMap.prototype.RemoveUndoMarker = function () {
            this._undoMarkers[this._undoMarkers.length - 1].setMap(null);
            this._undoMarkers.splice(this._undoMarkers.length - 1, 1);
        };
        // ****方法:移除全部UndoMarker *****
        EupMap.prototype.RemoveAllUndoMarkers = function () {
            for (var i = 0; i < this._undoMarkers.length; i++)
                this._undoMarkers[i].setMap(null);
            this._undoMarkers.splice(0, this._undoMarkers.length);
        };
        // ****方法:加入DeleteMarker click 事件 *****
        EupMap.prototype.AddClickEventOnDeleteMarker = function (deleteIndex, callBack) {
            var me = this;
            var currentMarker = this._deleteMarkers[deleteIndex];
            google.maps.event.addListener(currentMarker, 'click', function (event) {
                var cursor_gisXY = me._GetGisXYFromViewPixelXY(me._cursorX, me._cursorY); // ** for cursor **
                var point = me._GetScreenPoint_FromMapLeftTop(event.latLng.lng(), event.latLng.lat()); // ** for marker **
                var idx = me._FindDeleteMarkerIndex(currentMarker);
                if (idx >= 0) callBack(idx, event.latLng.lng(), event.latLng.lat(), point.x, point.y, cursor_gisXY.lng(), cursor_gisXY.lat(), me._cursorX, me._cursorY);
            });
        };
        // ****方法:尋找Deletemarker index *****
        EupMap.prototype._FindDeleteMarkerIndex = function (marker) {
            var len = this._deleteMarkers.length || 0;
            for (var i = 0; i < len; i++)
                if (this._deleteMarkers[i] == marker) return i;
            return -1;
        };
        // ****方法:加入UndoMarker click 事件 *****
        EupMap.prototype.AddClickEventOnUndoMarker = function (undoIndex, callBack) {
            var me = this;
            var currentMarker = this._undoMarkers[undoIndex];
            google.maps.event.addListener(currentMarker, 'click', function (event) {
                var cursor_gisXY = me._GetGisXYFromViewPixelXY(me._cursorX, me._cursorY); // ** for cursor **
                var point = me._GetScreenPoint_FromMapLeftTop(event.latLng.lng(), event.latLng.lat()); // ** for marker **
                var idx = me._FindUndoMarkerIndex(currentMarker);
                if (idx >= 0) callBack(idx, event.latLng.lng(), event.latLng.lat(), point.x, point.y, cursor_gisXY.lng(), cursor_gisXY.lat(), me._cursorX, me._cursorY);
            });
        };
        // ****方法:尋找Undomarker index *****
        EupMap.prototype._FindUndoMarkerIndex = function (marker) {
            var len = this._undoMarkers.length || 0;
            for (var i = 0; i < len; i++)
                if (this._undoMarkers[i] == marker) return i;
            return -1;
        };
        // ****方法:加入一Distance折線至地圖*******
        EupMap.prototype.AddDistanceLine = function (pointarry, geodesic, htmlColor, linestrokeopacity, lineWeight, zIndex) {
            var opt = {
                path: pointarry,
                geodesic: geodesic,
                strokeColor: htmlColor,
                strokeOpacity: linestrokeopacity,
                strokeWeight: lineWeight,
                zIndex: zIndex,
                map: this._map
            };
            var newLine = new google.maps.Polyline(opt);
            return this._distanceLines.push([newLine, opt]) - 1;
        };

        // ****方法:移除最後一DistanceLine********
        EupMap.prototype.RemoveLastDistanceLine = function () {
            this._distanceLines[this._distanceLines.length - 1][0].setMap(null);
            this._distanceLines.splice(this._distanceLines.length - 1, 1);
        };
        // ****方法:移除全部DistanceLine********
        EupMap.prototype.RemoveAllDistanceLines = function () {
            for (var i = 0; i < this._distanceLines.length; i++)
                this._distanceLines[i][0].setMap(null);
            this._distanceLines.splice(0, this._distanceLines.length);
        };
        // *** 方法:取得Line 數量 *****
        EupMap.prototype.GetAllDistanceLinesLenth = function () {
            return this._distanceLines.length;
        };
        // *** 方法:移除地圖 click 事件 *****
        EupMap.prototype.removeClickEventOnMap = function () {
            google.maps.event.clearListeners(this._map, 'click');
        }
        // ****方法:加入Marker rightclick 事件 *****
        EupMap.prototype.AddRightClickEventOnMarker = function (markerIndex, callBack) {
            var me = this;
            var currentMarker = this._allMarkers[markerIndex];
            google.maps.event.addListener(currentMarker, 'rightclick', function (event) {
                var cursor_gisXY = me._GetGisXYFromViewPixelXY(me._cursorX, me._cursorY); // ** for cursor **
                var point = me._GetScreenPoint_FromMapLeftTop(event.latLng.lng(), event.latLng.lat()); // ** for marker **
                var idx = me._FindMarkerIndex(currentMarker);
                if (idx >= 0) callBack(idx, event.latLng.lng(), event.latLng.lat(), point.x, point.y, cursor_gisXY.lng(), cursor_gisXY.lat(), me._cursorX, me._cursorY);
            });
        };

        // ****方法:帶marker加入Marker rightclick 事件 *****
        EupMap.prototype.AddRightClickEventOnMarkerByMarker = function (marker, markerIndex, callBack) {
            var me = this;
            google.maps.event.addListener(marker, 'rightclick', function (event) {
                var cursor_gisXY = me._GetGisXYFromViewPixelXY(me._cursorX, me._cursorY); // ** for cursor **
                var point = me._GetScreenPoint_FromMapLeftTop(event.latLng.lng(), event.latLng.lat()); // ** for marker **
                // var idx = me._FindMarkerIndex(marker);
                callBack(markerIndex, event.latLng.lng(), event.latLng.lat(), point.x, point.y, cursor_gisXY.lng(), cursor_gisXY.lat(), me._cursorX, me._cursorY);
            });
        };
        // ****方法:移除Marker rightclick 事件 *****
        EupMap.prototype.RemoveRightClickEventOnMarker = function (markerIndex) {
            google.maps.event.clearListeners(this._allMarkers[markerIndex], 'rightclick');
        };
        // ****方法:移除全部Marker rightclick 事件 *****
        EupMap.prototype.RemoveRightClickEventOnAllMarker = function () {
            for (var i = 0; i < this._allMarkers.length; i++)
                google.maps.event.clearListeners(this._allMarkers[i], 'rightclick');
        };
        // ****方法:加入Marker click 事件 *****
        EupMap.prototype.AddClickEventOnMarker = function (markerIndex, callBack) {
            var me = this;
            var currentMarker = this._allMarkers[markerIndex];
            google.maps.event.addListener(currentMarker, 'click', function (event) {
                var cursor_gisXY = me._GetGisXYFromViewPixelXY(me._cursorX, me._cursorY); // ** for cursor **
                var point = me._GetScreenPoint_FromMapLeftTop(event.latLng.lng(), event.latLng.lat()); // ** for marker **
                var idx = me._FindMarkerIndex(currentMarker);
                if (idx >= 0) callBack(idx, event.latLng.lng(), event.latLng.lat(), point.x, point.y, cursor_gisXY.lng(), cursor_gisXY.lat(), me._cursorX, me._cursorY);
            });
        };

        // ****方法:帶marker加入Marker click 事件 *****
        EupMap.prototype.AddClickEventOnMarkerByMarker = function (marker, markerIndex, callBack) {
            var me = this;
            google.maps.event.addListener(marker, 'click', function (event) {
                var cursor_gisXY = me._GetGisXYFromViewPixelXY(me._cursorX, me._cursorY); // ** for cursor **
                var point = me._GetScreenPoint_FromMapLeftTop(event.latLng.lng(), event.latLng.lat()); // ** for marker **
                callBack(markerIndex, event.latLng.lng(), event.latLng.lat(), point.x, point.y, cursor_gisXY.lng(), cursor_gisXY.lat(), me._cursorX, me._cursorY);
            });
        };
        // ****方法:移除Marker click 事件*****
        EupMap.prototype.RemoveClickEventOnMarker = function (markerIndex) {
            google.maps.event.clearListeners(this._allMarkers[markerIndex], 'click');
        };
        // ****方法:移除全部Marker click 事件*****
        EupMap.prototype.RemoveClickEventOnAllMarker = function () {
            for (var i = 0; i < this._allMarkers.length; i++)
                google.maps.event.clearListeners(this._allMarkers[i], 'click');
        };
        // ****方法:加入Marker dblclick 事件 *****
        EupMap.prototype.AddDblClickEventOnMarker = function (markerIndex, callBack) {
            var me = this;
            var currentMarker = this._allMarkers[markerIndex];
            google.maps.event.addListener(currentMarker, 'dblclick', function (event) {
                var cursor_gisXY = me._GetGisXYFromViewPixelXY(me._cursorX, me._cursorY); // ** for cursor **
                var point = me._GetScreenPoint_FromMapLeftTop(event.latLng.lng(), event.latLng.lat()); // ** for marker **
                var idx = me._FindMarkerIndex(currentMarker);
                if (idx >= 0) callBack(idx, event.latLng.lng(), event.latLng.lat(), point.x, point.y, cursor_gisXY.lng(), cursor_gisXY.lat(), me._cursorX, me._cursorY);
            });
        };
        // ****方法:帶marker加入Marker dblclick 事件 *****
        EupMap.prototype.AddDblClickEventOnMarkerByMarker = function (marker, markerIndex, callBack) {
            var me = this;
            google.maps.event.addListener(marker, 'dblclick', function (event) {
                var cursor_gisXY = me._GetGisXYFromViewPixelXY(me._cursorX, me._cursorY); // ** for cursor **
                var point = me._GetScreenPoint_FromMapLeftTop(event.latLng.lng(), event.latLng.lat()); // ** for marker **
                callBack(markerIndex, event.latLng.lng(), event.latLng.lat(), point.x, point.y, cursor_gisXY.lng(), cursor_gisXY.lat(), me._cursorX, me._cursorY);
            });
        };
        // ****方法:移除Marker dblclick 事件*****
        EupMap.prototype.RemoveDblClickEventOnMarker = function (markerIndex) {
            google.maps.event.clearListeners(this._allMarkers[markerIndex], 'dblclick');
        };
        // ****方法:移除全部Marker click 事件*****
        EupMap.prototype.RemoveDblClickEventOnAllMarker = function () {
            for (var i = 0; i < this._allMarkers.length; i++)
                google.maps.event.clearListeners(this._allMarkers[i], 'dblclick');
        };
        // ****方法:加入Marker drag事件*****
        EupMap.prototype.AddDragEventOnMarker = function (markerIndex, callBack) {
            var me = this;
            var currentMarker = this._allMarkers[markerIndex];
            google.maps.event.addListener(currentMarker, 'drag', function (event) {
                var cursor_gisXY = me._GetGisXYFromViewPixelXY(me._cursorX, me._cursorY); //for cursor
                var point = me._GetScreenPoint_FromMapLeftTop(event.latLng.lng(), event.latLng.lat()); //for marker
                var idx = me._FindMarkerIndex(currentMarker);
                if (idx >= 0) callBack(idx, event.latLng.lng(), event.latLng.lat(), point.x, point.y, cursor_gisXY.lng(), cursor_gisXY.lat(), me._cursorX, me._cursorY);
            });
        };
        // ****方法:移除Marker drag事件*****
        EupMap.prototype.RemoveDragEventOnMarker = function (markerIndex) {
            google.maps.event.clearListeners(this._allMarkers[markerIndex], 'drag');
        };
        // ****方法:移除全部Marker drag事件*****
        EupMap.prototype.RemoveDragEventOnAllMarker = function () {
            for (var i = 0; i < this._allMarkers.length; i++)
                google.maps.event.clearListeners(this._allMarkers[i], 'drag');
        };
        // ****方法:加入Marker dragend事件*****
        EupMap.prototype.AddDragendEventOnMarker = function (markerIndex, callBack) {
            var me = this;
            var currentMarker = this._allMarkers[markerIndex];
            google.maps.event.addListener(currentMarker, 'dragend', function (event) {
                var cursor_gisXY = me._GetGisXYFromViewPixelXY(me._cursorX, me._cursorY); //for cursor
                var point = me._GetScreenPoint_FromMapLeftTop(event.latLng.lng(), event.latLng.lat()); //for marker
                var idx = me._FindMarkerIndex(currentMarker);
                if (idx >= 0) callBack(idx, event.latLng.lng(), event.latLng.lat(), point.x, point.y, cursor_gisXY.lng(), cursor_gisXY.lat(), me._cursorX, me._cursorY);
            });
        };
        // ****方法:移除Marker dragend事件*****
        EupMap.prototype.RemoveDragendEventOnMarker = function (markerIndex) {
            google.maps.event.clearListeners(this._allMarkers[markerIndex], 'dragend');
        };
        // ****方法:移除全部Marker dragend事件*****
        EupMap.prototype.RemoveDragendEventOnAllMarker = function () {
            for (var i = 0; i < this._allMarkers.length; i++)
                google.maps.event.clearListeners(this._allMarkers[i], 'dragend');
        };
        // ****方法:設定Marker all 屬性*****
        EupMap.prototype.SetMarker = function (markerIndex, clickable, cursor, icon_url, icon_anchorX, icon_anchorY, gisX, gisY, title, visible, zIndex, draggable) {
            var mk = this._allMarkers[markerIndex];
            var opt = {};
            if (clickable != undefined) opt.clickable = clickable;
            if (cursor != undefined) opt.cursor = cursor;
            opt.icon = mk.getIcon();
            if (icon_url != undefined) opt.icon.url = icon_url;
            if (icon_anchorX != undefined && icon_anchorY != undefined)
                opt.icon.anchor = new google.maps.Point(icon_anchorX, icon_anchorY);
            if (gisX != undefined && gisY != undefined) opt.position = new google.maps.LatLng(gisY, gisX);
            if (title != undefined) opt.title = title;
            if (visible != undefined) opt.visible = visible;
            if (zIndex != undefined) opt.zIndex = zIndex;
            if (draggable != undefined) opt.draggable = draggable;
            mk.setOptions(opt);
        };
        // ****方法:設定Marker Anchor*****
        EupMap.prototype.SetMarkerAnchor = function (markerIndex, x, y) {
            this.SetMarker(markerIndex, undefined, undefined, undefined, x, y);
        };
        // ****方法:移動Marker*****
        EupMap.prototype.MoveMarker = function (markerIndex, gisX, gisY) {
            this._allMarkers[markerIndex].setPosition(new google.maps.LatLng(gisY, gisX));
        };

        // ****方法:滑動Marker*****
        EupMap.prototype.MoveMarkerAnimate = function (markerIndex, gisX, gisY, minisecond) {
            var section = minisecond / 10;
            var marker = this._allMarkers[markerIndex];
            var step = {
                x: (gisX - marker.getPosition().lng()) / section,
                y: (gisY - marker.getPosition().lat()) / section
            };
            for (var i = 0; i < section; i++) {
                setTimeout(function () {
                    try {
                        var position = marker.getPosition();
                        marker.setPosition(new google.maps.LatLng(position.lat() + step.y, position.lng() + step.x));
                    } catch (e) {
                        console.log(e.message);
                    }
                }, i * 10);
            }
        };

        // ****方法:設定Marker Visible *****
        EupMap.prototype.SetMarkerVisible = function (markerIndex, isVisible) {
            this._allMarkers[markerIndex].setVisible(isVisible);
        };

        // ****方法:設定Marker資訊視窗 *****
        EupMap.prototype.SetMarkerinfowindows = function (markerIndex, contentinfo, zindex, callback) {
            this._allMarkers[markerIndex].info = new google.maps.InfoWindow({
                //when I add <IMG BORDER="0" ALIGN="Left" SRC="stagleton.jpg"> the maps will not load
                content: contentinfo,
                zIndex: zindex || 10000
            });
            this._allMarkers[markerIndex].info.open(this._map, this._allMarkers[markerIndex]);
            if (callback)
                google.maps.event.addListener(this._allMarkers[markerIndex].info, 'domready', callback);
        };

        // ****方法:設定Polygon資訊視窗 *****
        EupMap.prototype.SetPolygoninfowindows = function (markerIndex, contentinfo, zindex, callback) {
            this._allMarkers[markerIndex].info = new google.maps.InfoWindow({
                //when I add <IMG BORDER="0" ALIGN="Left" SRC="stagleton.jpg"> the maps will not load
                content: contentinfo,
                zIndex: zindex || 10000
            });
            this._allMarkers[markerIndex].info.open(this._map, this._allMarkers[markerIndex]);
            if (callback)
                google.maps.event.addListener(this._allMarkers[markerIndex].info, 'domready', callback);
        };

        // ****方法:移除Marker資訊視窗 *****
        EupMap.prototype.DeleteMarkerinfowindows = function (markerIndex) {
            this._allMarkers[markerIndex].info.close();
        };

        // ****方法:移除全部Marker資訊視窗 *****
        EupMap.prototype.DeleteAllMarkerinfowindows = function () {
            this._allMarkers.forEach(function (m) {
                if (m.hasOwnProperty('info')) m.info.close();
            });
        };
        // ****方法:設定Marker Icon*****
        EupMap.prototype.SetMarkerIcon = function (markerIndex, newImg) {
            this.SetMarker(markerIndex, undefined, undefined, newImg);
        };
        // ****方法:設定Marker Title*******
        EupMap.prototype.SetMarkerTitle = function (markerIndex, title) {
            this._allMarkers[markerIndex].setTitle(title);
        };
        // ****方法:設定Marker Draggable 屬性******
        EupMap.prototype.SetMarkerDraggable = function (markerIndex, draggable) {
            this._allMarkers[markerIndex].setDraggable(draggable);
        };
        // ****方法:取得Marker Draggable 屬性******
        EupMap.prototype.GetMarkerDraggable = function (markerIndex) {
            var result = this._allMarkers[markerIndex].getDraggable();
            return result == undefined ? false : result;
        };
        // ****方法:取得MarkerGis X *****
        EupMap.prototype.GetMarkerGisX = function (markerIndex) {
            var pnt = this._allMarkers[markerIndex].getPosition();
            return pnt.lng();
        };
        // ****方法:取得MarkerGis Y *****
        EupMap.prototype.GetMarkerGisY = function (markerIndex) {
            var pnt = this._allMarkers[markerIndex].getPosition();
            return pnt.lat();
        };
        // ****方法:取得Marker經緯度*****
        EupMap.prototype.GetMarkerGisXY = function (markerIndex) {
            var pnt = this._allMarkers[markerIndex].getPosition();
            return pnt.lat() + "," + pnt.lng();
        };
        // ****方法:設定 Marker z-index******
        EupMap.prototype.SetMarkerZIndex = function (markerIndex, zIndex) {
            this._allMarkers[markerIndex].setZIndex(zIndex);
        };
        // ****方法:取得Marker z-index*****
        EupMap.prototype.GetMarkerZIndex = function (markerIndex) {
            return this._allMarkers[markerIndex].getZIndex();
        };
        // ****方法:取得Marker z-index  最大值*******
        EupMap.prototype.GetMarkerZIndexMax = function () {
            var currentMarker;
            var z_index;
            var max = Number.NEGATIVE_INFINITY;
            for (var i = 0; i < this._allMarkers.length; i++) {
                currentMarker = this._allMarkers[i];
                z_index = currentMarker.getZIndex();
                if (!isNaN(z_index)) max = Math.max(max, z_index);
            }
            if (max != Number.NEGATIVE_INFINITY) return max;
            else return null;
        };
        // ****方法:取得Marker Icon Img*******
        EupMap.prototype.GetMarkerIcon = function (markerIndex) {
            return this._allMarkers[markerIndex].getIcon().url;
        };
        // ****方法:加入一折線至地圖*******
        EupMap.prototype.AddOneLine = function (gisX0, gisY0, gisX1, gisY1, htmlColor, lineWeight, zIndex, lineOpacity, morePathStr) {
            var pnts = [
                new google.maps.LatLng(gisY0, gisX0),
                new google.maps.LatLng(gisY1, gisX1)
            ];
            var opt = {
                path: pnts,
                strokeColor: htmlColor,
                strokeOpacity: 1.0,
                strokeWeight: lineWeight,
                zIndex: zIndex,
                map: this._map
            };
            if (lineOpacity != undefined) opt.strokeOpacity = lineOpacity;
            if (morePathStr != undefined) {
                var morePath = morePathStr.split("|");
                for (var i = 0; i < morePath.length; i++) {
                    var ary = morePath[i].split(",");
                    pnts.push(new google.maps.LatLng(Number(ary[1]), Number(ary[0])));
                }
            }
            var newLine = new google.maps.Polyline(opt);
            return this._allLines.push([newLine, opt]) - 1;
        };

        // ****方法:加入一折線至地圖*******
        EupMap.prototype.AddAllLine = function (pointarry, geodesic, htmlColor, linestrokeopacity, lineWeight, zIndex) {
            var opt = {
                path: pointarry,
                geodesic: geodesic,
                strokeColor: htmlColor,
                strokeOpacity: linestrokeopacity,
                strokeWeight: lineWeight,
                zIndex: zIndex,
                map: this._map
            };

            var newLine = new google.maps.Polyline(opt);
            return this._allLines.push([newLine, opt]) - 1;
        };

        // ****方法:加入Line rightclick 事件*******
        EupMap.prototype.AddRightClickEventOnLine = function (LineIndex, callBack) {
            var me = this;
            var currentLine = this._allLines[LineIndex][0];
            google.maps.event.addListener(currentLine, 'rightclick', function (event) {
                var point = me._GetScreenPoint_FromMapLeftTop(event.latLng.lng(), event.latLng.lat());
                var idx = me._FindLineIndex(currentLine);
                if (idx >= 0) callBack(idx, event.latLng.lng(), event.latLng.lat(), point.x, point.y, (event.edge == undefined ? -1 : event.edge), (event.path == undefined ? -1 : event.path), (event.vertex == undefined ? -1 : event.vertex));
            });
        };
        // ****方法:移除Line rightclick 事件******
        EupMap.prototype.RemoveRightClickEventOnLine = function (LineIndex) {
            var currentLine = this._allLines[LineIndex][0];
            google.maps.event.clearListeners(currentLine, 'rightclick');
        };
        // ****方法:移除全部Line rightclick 事件******
        EupMap.prototype.RemoveRightClickEventOnAllLine = function () {
            for (var i = 0; i < this._allLines.length; i++)
                this.RemoveRightClickEventOnLine(i);
        };
        // ****方法:加入Line click 事件*******
        EupMap.prototype.AddClickEventOnLine = function (LineIndex, callBack) {
            var me = this;
            var currentLine = this._allLines[LineIndex][0];
            google.maps.event.addListener(currentLine, 'click', function (event) {
                var point = me._GetScreenPoint_FromMapLeftTop(event.latLng.lng(), event.latLng.lat());
                var idx = me._FindLineIndex(currentLine);
                if (idx >= 0) callBack(idx, event.latLng.lng(), event.latLng.lat(), point.x, point.y, (event.edge == undefined ? -1 : event.edge), (event.path == undefined ? -1 : event.path), (event.vertex == undefined ? -1 : event.vertex));
            });
        };
        // ****方法:移除Line click 事件*******
        EupMap.prototype.RemoveClickEventOnLine = function (LineIndex) {
            google.maps.event.clearListeners(this._allLines[LineIndex][0], 'click');
        };
        // ****方法:移除全部Line click 事件******
        EupMap.prototype.RemoveClickEventOnAllLine = function () {
            for (var i = 0; i < this._allLines.length; i++)
                this.RemoveClickEventOnLine(i);
        };
        // ****方法:加入Line dblclick 事件*******
        EupMap.prototype.AddDblClickEventOnLine = function (LineIndex, callBack) {
            var me = this;
            var currentLine = this._allLines[LineIndex][0];
            google.maps.event.addListener(currentLine, 'dblclick', function (event) {
                var point = me._GetScreenPoint_FromMapLeftTop(event.latLng.lng(), event.latLng.lat());
                var idx = me._FindLineIndex(currentLine);
                if (idx >= 0) callBack(idx, event.latLng.lng(), event.latLng.lat(), point.x, point.y, (event.edge == undefined ? -1 : event.edge), (event.path == undefined ? -1 : event.path), (event.vertex == undefined ? -1 : event.vertex));
            });
        };
        // ****方法:移除Line dblclick 事件*******
        EupMap.prototype.RemoveDblClickEventOnLine = function (LineIndex) {
            google.maps.event.clearListeners(this._allLines[LineIndex][0], 'dblclick');
        };
        // ****方法:移除全部Line dblclick 事件******
        EupMap.prototype.RemoveDblClickEventOnAllLine = function () {
            for (var i = 0; i < this._allLines.length; i++)
                this.RemoveDblClickEventOnLine(i);
        };
        // ****方法:加入Line mouseup 事件******
        EupMap.prototype.AddMouseupEventOnLine = function (lineIndex, callBack) {
            var me = this;
            var currentLine = this._allLines[lineIndex][0];
        };
        // ****方法:移除Line mouseup 事件*****
        EupMap.prototype.RemoveMouseupEventOnLine = function (index) {
            google.maps.event.clearListeners(this._allLines[index][0], 'mouseup');
        };
        // ****方法:移除全部Line mouseup 事件******
        EupMap.prototype.RemoveMouseupEventOnAllLine = function () {
            for (i = 0; i < this._allLines.length; i++)
                google.maps.event.clearListeners(this._allLines[i][0], 'mouseup');
        };
        // ****方法:取得目前Lines總數量******
        EupMap.prototype.GetAllLinesLenth = function () {
            return this._allLines.length;
        };
        // ****方法:移除特定一Line********
        EupMap.prototype.RemoveLine = function (index) {
            this._allLines[index][0].setMap(null);
            this._allLines.splice(index, 1);
        };

        // ****方法:移除特定一Line********
        EupMap.prototype.RemoveLastLine = function () {
            this._allLines[this._allLines.length - 1][0].setMap(null);
            this._allLines.splice(this._allLines.length - 1, 1);
        };

        // ****方法:移除全部Line********
        EupMap.prototype.RemoveAllLines = function () {
            for (var i = 0; i < this._allLines.length; i++)
                this._allLines[i][0].setMap(null);
            this._allLines.splice(0, this._allLines.length);
        };
        // ****方法:取得目前Polygon 路徑點資訊*****
        EupMap.prototype.GetLinePath = function (index) {
            var pnts = this._allLines[index][0].getPath();
            var pntsStr = "";
            var i = 0;
            var latLng;
            for (i = 0; i < pnts.getLength(); i++) {
                latLng = pnts.getAt(i);
                pntsStr += latLng.lng() + "," + latLng.lat();
                if (i < pnts.getLength() - 1)
                    pntsStr += "|";
            }
            return pntsStr;
        };
        // ****方法:取得Line  zIndex*******
        EupMap.prototype.GetLineZIndex = function (index) {
            var options = this._allLines[index][1];
            return options.zIndex;
        };
        // ****方法:取得Line z-index  最大值*****
        EupMap.prototype.GetLineZIndexMax = function () {
            var currentLineOpts;
            var z_index;
            var max = Number.NEGATIVE_INFINITY;
            for (var i = 0; i < this._allLines.length; i++) {
                currentLineOpts = this._allLines[i][1];
                z_index = currentLineOpts.zIndex;
                if (!isNaN(z_index)) max = Math.max(max, z_index);
            }
            if (max != Number.NEGATIVE_INFINITY) return max;
            else return null;
        };
        // ****方法:設定Line  zIndex******
        EupMap.prototype.SetLineZIndex = function (index, zIndex) {
            var line = this._allLines[index][0];
            var options = this._allLines[index][1];
            options.zIndex = zIndex;
            options.paths = line.getPath();
            line.setOptions(options);
        };

        // ****方法:設定Line  是否顯示
        EupMap.prototype.SetLinevisible = function (index, visible) {
            var line = this._allLines[index][0];
            var options = this._allLines[index][1];
            options.visible = visible;
            line.setOptions(options);
        };

        // ****方法:設定Line  editable******
        EupMap.prototype.SetLineEditable = function (index, editable) {
            var line = this._allLines[index][0];
            var options = this._allLines[index][1];
            options.editable = editable;
            options.paths = line.getPath();
            line.setOptions(options);
        };
        // ****方法:取得Line  editable******
        EupMap.prototype.GetLineEditable = function (index) {
            return this._allLines[index][0].getEditable() || false;
        };
        // ****方法:加入一多邊形*****
        EupMap.prototype.AddOnePolygon = function (p_pathsArryStr, p_strokeColor, p_strokeOpacity, p_strokeWeight, p_fillColor, p_fillOpacity, p_zIndex) {
            var options = {
                paths: p_pathsArryStr,
                strokeColor: p_strokeColor,
                strokeOpacity: p_strokeOpacity,
                strokeWeight: p_strokeWeight,
                fillColor: p_fillColor,
                fillOpacity: p_fillOpacity,
                zIndex: p_zIndex,
                map: this._map
            };
            var newPolygon = new google.maps.Polygon(options);
            return this._allPolygon.push([newPolygon, options]) - 1;
        };
        // ****方法:取得目前Polygon總數量*****
        EupMap.prototype.GetAllPolygonsLenth = function () {
            return this._allPolygon.length;
        };
        // ****方法:取得目前Polygon 路徑點資訊*****
        EupMap.prototype.GetPolygonPath = function (index) {
            var pnts = this._allPolygon[index][0].getPath();
            var pntsStr = "";
            var i = 0;
            var latLng;
            for (i = 0; i < pnts.getLength(); i++) {
                latLng = pnts.getAt(i);
                pntsStr += latLng.lng() + "," + latLng.lat();
                if (i < pnts.getLength() - 1)
                    pntsStr += "|";
            }
            return pntsStr;
        };
        // ****方法:取得目前Polygon 路徑點資訊*****
        EupMap.prototype.GetPolygonPathByObject = function (Polygon) {
            var pnts = Polygon[0].getPath();
            var tempAry = [];
            var tempObj = {};
            for (var i = 0; i < pnts.getLength(); i++) {
                tempObj = {};
                latLng = pnts.getAt(i);
                tempObj.lng = latLng.lng();
                tempObj.lat = latLng.lat();
                tempAry.push(tempObj);
            }
            return tempAry;
        };
        // ****方法:移除特定一Polygon********
        EupMap.prototype.RemovePolygon = function (index) {
            this._allPolygon[index][0].setMap(null);
            this._allPolygon.splice(index, 1);
        };
        // ****方法:移除所有多邊形*******
        EupMap.prototype.RemoveAllPolygons = function () {
            for (var i = 0; i < this._allPolygon.length; i++)
                this._allPolygon[i][0].setMap(null);
            this._allPolygon.splice(0, this._allPolygon.length);
        };
        // ****方法:加入Polygon rightclick 事件*******
        EupMap.prototype.AddRightClickEventOnPolygon = function (polygonIndex, callBack) {
            var me = this;
            var currentPolygon = this._allPolygon[polygonIndex][0];
            google.maps.event.addListener(currentPolygon, 'rightclick', function (event) {
                var point = me._GetScreenPoint_FromMapLeftTop(event.latLng.lng(), event.latLng.lat());
                var idx = me._FindPolygonIndex(currentPolygon);
                if (idx >= 0) callBack(idx, event.latLng.lng(), event.latLng.lat(), point.x, point.y, (event.edge == undefined ? -1 : event.edge), (event.path == undefined ? -1 : event.path), (event.vertex == undefined ? -1 : event.vertex));
            });
        };
        // ****方法:移除Polygon rightclick 事件*****
        EupMap.prototype.RemoveRightClickEventOnPolygon = function (index) {
            google.maps.event.clearListeners(this._allPolygon[index][0], 'rightclick');
        };
        // ****方法:移除全部Polygon rightclick 事件*****
        EupMap.prototype.RemoveRightClickEventOnAllPolygon = function () {
            for (i = 0; i < this._allPolygon.length; i++)
                google.maps.event.clearListeners(this._allPolygon[i][0], 'rightclick');
        };
        // ****方法:加入Polygon click 事件*******
        EupMap.prototype.AddClickEventOnPolygon = function (polygonIndex, callBack) {
            var me = this;
            var currentPolygon = this._allPolygon[polygonIndex][0];
            google.maps.event.addListener(currentPolygon, 'click', function (event) {
                var point = me._GetScreenPoint_FromMapLeftTop(event.latLng.lng(), event.latLng.lat());
                var idx = me._FindPolygonIndex(currentPolygon);
                if (idx >= 0) callBack(idx, event.latLng.lng(), event.latLng.lat(), point.x, point.y, (event.edge == undefined ? -1 : event.edge), (event.path == undefined ? -1 : event.path), (event.vertex == undefined ? -1 : event.vertex));
            });
        };
        // ****方法:移除Polygon click 事件*******
        EupMap.prototype.RemoveClickEventOnPolygon = function (index) {
            google.maps.event.clearListeners(this._allPolygon[index][0], 'click');
        };
        // ****方法:移除全部Polygon click 事件******
        EupMap.prototype.RemoveClickEventOnAllPolygon = function () {
            for (i = 0; i < this._allPolygon.length; i++)
                google.maps.event.clearListeners(this._allPolygon[i][0], 'click');
        };
        // ****方法:加入Polygon dblclick 事件*******
        EupMap.prototype.AddDblClickEventOnPolygon = function (polygonIndex, callBack) {
            var me = this;
            var currentPolygon = this._allPolygon[polygonIndex][0];
            google.maps.event.addListener(currentPolygon, 'dblclick', function (event) {
                var point = me._GetScreenPoint_FromMapLeftTop(event.latLng.lng(), event.latLng.lat());
                var idx = me._FindPolygonIndex(currentPolygon);
                if (idx >= 0) callBack(idx, event.latLng.lng(), event.latLng.lat(), point.x, point.y, (event.edge == undefined ? -1 : event.edge), (event.path == undefined ? -1 : event.path), (event.vertex == undefined ? -1 : event.vertex));
            });
        };
        // ****方法:移除Polygon dblclick 事件*******
        EupMap.prototype.RemoveDblClickEventOnPolygon = function (index) {
            google.maps.event.clearListeners(this._allPolygon[index][0], 'dblclick');
        };
        // ****方法:移除全部Polygon dblclick 事件******
        EupMap.prototype.RemoveDblClickEventOnAllPolygon = function () {
            for (i = 0; i < this._allPolygon.length; i++)
                google.maps.event.clearListeners(this._allPolygon[i][0], 'dblclick');
        };
        // ****方法:加入Polygon mouseup 事件******
        EupMap.prototype.AddMouseupEventOnPolygon = function (polygonIndex, callBack) {
            var me = this;
            var currentPolygon = this._allPolygon[polygonIndex][0];
            google.maps.event.addListener(currentPolygon, 'mouseup', function (event) {
                var point = me._GetScreenPoint_FromMapLeftTop(event.latLng.lng(), event.latLng.lat());
                var idx = me._FindPolygonIndex(currentPolygon);
                if (idx >= 0) callBack(idx, event.latLng.lng(), event.latLng.lat(), point.x, point.y, (event.edge == undefined ? -1 : event.edge), (event.path == undefined ? -1 : event.path), (event.vertex == undefined ? -1 : event.vertex));
            });
        };
        // ****方法:移除Polygon mouseup 事件*****
        EupMap.prototype.RemoveMouseupEventOnPolygon = function (index) {
            google.maps.event.clearListeners(this._allPolygon[index][0], 'mouseup');
        };
        // ****方法:移除全部Polygon mouseup 事件******
        EupMap.prototype.RemoveMouseupEventOnAllPolygon = function () {
            for (i = 0; i < this._allPolygon.length; i++)
                google.maps.event.clearListeners(this._allPolygon[i][0], 'mouseup');
        };
        // ****方法:取得Polygon  zIndex********
        EupMap.prototype.GetPolygonZIndex = function (index) {
            return this._allPolygon[index][1].zIndex;
        };
        // ****方法:取得Polygon z-index  最大值********
        EupMap.prototype.GetPolygonZIndexMax = function () {
            var currentPolygonOpts;
            var z_index;
            var max = Number.NEGATIVE_INFINITY;
            for (var i = 0; i < this._allPolygon.length; i++) {
                currentPolygonOpts = this._allPolygon[i][1];
                z_index = currentPolygonOpts.zIndex;
                if (!isNaN(z_index)) max = Math.max(max, z_index);
            }
            if (max != Number.NEGATIVE_INFINITY) return max;
            else return null;
        };
        // ****方法:設定Polygon  zIndex******
        EupMap.prototype.SetPolygonZIndex = function (index, zIndex) {
            var polygon = this._allPolygon[index][0];
            var options = this._allPolygon[index][1];
            options.zIndex = zIndex;
            options.paths = polygon.getPaths();
            polygon.setOptions(options);
        };
        // ****方法:設定Polygon Editable屬性******
        EupMap.prototype.SetPolygonEditable = function (index, editable) {
            var polygon = this._allPolygon[index][0];
            var options = this._allPolygon[index][1];
            options.editable = editable;
            options.paths = polygon.getPaths();
            polygon.setEditable(editable)
        };
        // ****方法:取得Polygon Editable屬性*****
        EupMap.prototype.GetPolygonEditable = function (index) {
            return this._allPolygon[index][0].getEditable() || false;
        };
        // ****方法:加入Label******
        EupMap.prototype.AddLabel = function (gisX, gisY, text, textColor, zIndex, MapPanesName, isVisible, AnchorX, AnchorY) {
            var gisXY = new google.maps.LatLng(gisY, gisX);
            var overlay = new CustOverlayLabel(gisXY, text, textColor, zIndex, MapPanesName, isVisible, AnchorX, AnchorY);
            overlay.setMap(this._map);
            return this._allLabels.push(overlay) - 1;
        };
        /**
         * Add by thuypx 24/09/2018
         * @param index
         * @constructor
         */
        EupMap.infowindow = null;
        EupMap.prototype.AddWindowInfo = function (index, content) {
            if (EupMap.infowindow) {
                EupMap.infowindow.close();
            }
            var marker = this._pointMarkers[index];
            EupMap.infowindow = new google.maps.InfoWindow({
                content: content
            });
            EupMap.infowindow.open(this._map, marker);
            //return this._allLabels.push(overlay) - 1;
        }
        EupMap.prototype.RemoveWindowInfo = function () {
            if (EupMap.infowindow) {
                EupMap.infowindow.close();
            }
        }

        // ****方法:移除指定Label******
        EupMap.prototype.RemoveLabel = function (index) {
            this._allLabels[index].setMap(null);
            this._allLabels.splice(index, 1);
        };
        // ****方法:移除全部Label*******
        EupMap.prototype.RemoveAllLabel = function () {
            for (var i = 0; i < this._allLabels.length; i++)
                this._allLabels[i].setMap(null);
            this._allLabels.splice(0, this._allLabels.length);
        };
        // ****方法:更新Label*******
        EupMap.prototype.UpdateLabel = function (index, gisX, gisY, text, textColor, zIndex, MapPanesName, isVisible) {
            this._allLabels[index].update(new google.maps.LatLng(gisY, gisX), text, textColor, zIndex, MapPanesName, isVisible);
        };
        // ****方法:取得多少Label*******
        EupMap.prototype.GetHowManyLabel = function () {
            return this._allLabels.length;
        };
        // ****方法:移動Label******
        EupMap.prototype.MoveLabel = function (index, gisX, gisY) {
            this._allLabels[index].move(new google.maps.LatLng(gisY, gisX));
        };
        // ****方法:更改Label 文字******
        EupMap.prototype.SetLabelText = function (index, text) {
            this._allLabels[index].setText(text);
        };
        // ****方法:更改Label 文字顏色****
        EupMap.prototype.SetLabelTextColor = function (index, textColor) {
            this._allLabels[index].setTextColor(textColor);
        };
        // ****方法:更改Label z index*****
        EupMap.prototype.SetLabelZIndex = function (index, z_index) {
            this._allLabels[index].setZIndex(z_index);
        };
        // ****方法:更改Label MapPanesName******
        EupMap.prototype.SetLabelMapPanesName = function (index, MapPanesName) {
            this._allLabels[index].setMapPanes(MapPanesName);
        };
        // ****方法:更改Label Visible*****
        EupMap.prototype.SetLabelIsVisible = function (index, isVisible) {
            var overlay = this._allLabels[index];
            if (isVisible) overlay.show();
            else overlay.hide();
        };
        // ****方法:加入一圓形*****
        EupMap.prototype.AddOneCircleBindToMarker = function (markerIndex, p_centerX, p_centerY, p_clickable, p_fillColor, p_fillOpacity, p_radius, p_strokeColor, p_strokeOpacity, p_strokeWeight, p_zIndex) {
            var options = {
                center: new google.maps.LatLng(p_centerY, p_centerX),
                clickable: p_clickable,
                fillColor: p_fillColor,
                fillOpacity: p_fillOpacity,
                map: this._map,
                radius: p_radius / 2,
                strokeColor: p_strokeColor,
                strokeOpacity: p_strokeOpacity,
                strokeWeight: p_strokeWeight,
                zIndex: p_zIndex
            };
            var newCircle = new google.maps.Circle(options);
            var marker = this._allMarkers[markerIndex];
            newCircle.bindTo('center', marker, 'position');
            return this._allCircle.push([newCircle, options]) - 1;
        };
        EupMap.prototype.AddOneCircle = function (p_centerX, p_centerY, p_clickable, p_fillColor, p_fillOpacity, p_radius, p_strokeColor, p_strokeOpacity, p_strokeWeight, p_zIndex) {
            var options = {
                center: new google.maps.LatLng(p_centerY, p_centerX),
                clickable: p_clickable,
                fillColor: p_fillColor,
                fillOpacity: p_fillOpacity,
                map: this._map,
                radius: p_radius / 2,
                strokeColor: p_strokeColor,
                strokeOpacity: p_strokeOpacity,
                strokeWeight: p_strokeWeight,
                zIndex: p_zIndex
            };
            var newCircle = new google.maps.Circle(options);
            return this._allCircle.push([newCircle, options]) - 1;
        };
        // ****方法:回傳所增加的圓形*****
        EupMap.prototype.GetAddOneCircle = function (p_centerX, p_centerY, p_clickable, p_fillColor, p_fillOpacity, p_radius, p_strokeColor, p_strokeOpacity, p_strokeWeight, p_zIndex) {
            
            var options = {
                center: new google.maps.LatLng(p_centerY, p_centerX),
                clickable: p_clickable,
                fillColor: p_fillColor,
                fillOpacity: p_fillOpacity,
                map: this._map,
                radius: p_radius / 2,
                strokeColor: p_strokeColor,
                strokeOpacity: p_strokeOpacity,
                strokeWeight: p_strokeWeight,
                zIndex: p_zIndex
            };
            var newCircle = new google.maps.Circle(options);
            this._allCircle.push([newCircle, options]);
            return newCircle;
        };
        // ****方法:取得目前Circle總數量*******
        EupMap.prototype.GetAllCircleLenth = function () {
            return this._allCircle.length;
        };
        // ****方法:移除特定一Circle*****
        EupMap.prototype.RemoveCircle = function (index) {
            this._allCircle[index][0].setMap(null);
            this._allCircle.splice(index, 1);
        };
        // ****方法:移除全部Circle*****
        EupMap.prototype.RemoveAllCircle = function () {
            for (var i = 0; i < this._allCircle.length; i++)
                this._allCircle[i][0].setMap(null);
            this._allCircle.splice(0, this._allCircle.length);
        };
        // ****方法:加入Circle rightclick 事件****
        EupMap.prototype.AddRightClickEventOnCircle = function (circleIndex, callBack) {
            var me = this;
            var currentCircle = this._allCircle[circleIndex][0];
            google.maps.event.addListener(currentCircle, 'rightclick', function (event) {
                var point = me._GetScreenPoint_FromMapLeftTop(event.latLng.lng(), event.latLng.lat());
                var idx = me._FindCircleIndex(currentCircle);
                if (idx >= 0) callBack(idx, event.latLng.lng(), event.latLng.lat(), point.x, point.y);
            });
        };
        // ****方法:移除Circle rightclick 事件*****
        EupMap.prototype.RemoveRightClickEventOnCircle = function (i) {
            google.maps.event.clearListeners(this._allCircle[i][0], 'rightclick');
        };
        // ****方法:移除所有Circle rightclick 事件*****
        EupMap.prototype.RemoveRightClickEventOnAllCircle = function () {
            for (var i = 0; i < this._allCircle.length; i++)
                google.maps.event.clearListeners(this._allCircle[i][0], 'rightclick');
        };
        // ****方法:加入Circle click 事件*****
        EupMap.prototype.AddClickEventOnCircle = function (circleIndex, callBack) {
            var me = this;
            var currentCircle = this._allCircle[circleIndex][0];
            google.maps.event.addListener(currentCircle, 'click', function (event) {
                var point = me._GetScreenPoint_FromMapLeftTop(event.latLng.lng(), event.latLng.lat());
                var idx = me._FindCircleIndex(currentCircle);
                if (idx >= 0) callBack(idx, event.latLng.lng(), event.latLng.lat(), point.x, point.y);
            });
        };
        // ****方法:移除Circle click 事件****
        EupMap.prototype.RemoveClickEventOnCircle = function (i) {
            google.maps.event.clearListeners(this._allCircle[i][0], 'click');
        };
        // ****方法:移除所有Circle click 事件*****
        EupMap.prototype.RemoveClickEventOnAllCircle = function () {
            for (var i = 0; i < this._allCircle.length; i++)
                google.maps.event.clearListeners(this._allCircle[i][0], 'click');
        };
        // ****方法:加入Circle dblclick 事件*****
        EupMap.prototype.AddDblClickEventOnCircle = function (circleIndex, callBack) {
            var me = this;
            var currentCircle = this._allCircle[circleIndex][0];
            google.maps.event.addListener(currentCircle, 'dblclick', function (event) {
                var point = me._GetScreenPoint_FromMapLeftTop(event.latLng.lng(), event.latLng.lat());
                var idx = me._FindCircleIndex(currentCircle);
                if (idx >= 0) callBack(idx, event.latLng.lng(), event.latLng.lat(), point.x, point.y);
            });
        };
        // ****方法:移除Circle dblclick 事件****
        EupMap.prototype.RemoveDblClickEventOnCircle = function (i) {
            google.maps.event.clearListeners(this._allCircle[i][0], 'dblclick');
        };
        // ****方法:移除所有Circle dblclick 事件*****
        EupMap.prototype.RemoveDblClickEventOnAllCircle = function () {
            for (var i = 0; i < this._allCircle.length; i++)
                google.maps.event.clearListeners(this._allCircle[i][0], 'dblclick');
        };
        // ****方法:加入Circle mouseup 事件*****
        EupMap.prototype.AddMouseupEventOnCircle = function (circleIndex, callBack) {
            var me = this;
            var currentCircle = this._allCircle[circleIndex][0];
            google.maps.event.addListener(currentCircle, 'mouseup', function (event) {
                var point = me._GetScreenPoint_FromMapLeftTop(event.latLng.lng(), event.latLng.lat());
                var idx = me._FindCircleIndex(currentCircle);
                if (idx >= 0) callBack(idx, event.latLng.lng(), event.latLng.lat(), point.x, point.y);
            });
        };
        // ****方法:移除Circle mouseup 事件*****
        EupMap.prototype.RemoveMouseupEventOnCircle = function (i) {
            google.maps.event.clearListeners(this._allCircle[i][0], 'mouseup');
        };
        // ****方法:移除所有Circle mouseup 事件*****
        EupMap.prototype.RemoveMouseupEventOnAllCircle = function () {
            for (var i = 0; i < this._allCircle.length; i++)
                google.maps.event.clearListeners(this._allCircle[i][0], 'mouseup');
        };
        // ****方法:加入Circle center_changed 事件*****
        EupMap.prototype.AddCenterChangedEventOnCircle = function (circleIndex, callBack) {
            var me = this;
            var currentCircle = this._allCircle[circleIndex][0];
            google.maps.event.addListener(currentCircle, 'center_changed', function () {
                var idx = me._FindCircleIndex(currentCircle);
                if (idx >= 0) callBack(idx);
            });
        };
        // ****方法:移除Circle center_changed 事件*****
        EupMap.prototype.RemoveCenterChangedEventOnCircle = function (i) {
            google.maps.event.clearListeners(this._allCircle[i][0], 'center_changed');
        };
        // ****方法:移除所有Circle center_changed 事件******
        EupMap.prototype.RemoveCenterChangedEventOnAllCircle = function () {
            for (var i = 0; i < this._allCircle.length; i++)
                google.maps.event.clearListeners(this._allCircle[i][0], 'center_changed');
        };
        // ****方法:加入Circle radius_changed 事件****
        EupMap.prototype.AddRadiusChangedEventOnCircle = function (circleIndex, callBack) {
            var me = this;
            var currentCircle = this._allCircle[circleIndex][0];
            google.maps.event.addListener(currentCircle, 'radius_changed', function () {
                var idx = me._FindCircleIndex(currentCircle);
                if (idx >= 0) callBack(idx);
            });
        };
        // ****方法:移除Circle radius_changed 事件*****
        EupMap.prototype.RemoveRadiusChangedEventOnCircle = function (i) {
            google.maps.event.clearListeners(this._allCircle[i][0], 'radius_changed');
        };
        // ****方法:移除所有Circle radius_changed 事件*****
        EupMap.prototype.RemoveRadiusChangedEventOnAllCircle = function () {
            for (var i = 0; i < this._allCircle.length; i++)
                google.maps.event.clearListeners(this._allCircle[i][0], 'radius_changed');
        };
        // ****方法:設定Circle Editable屬性****
        EupMap.prototype.SetCircleEditable = function (index, editable) {
            var circle = this._allCircle[index][0];
            var options = this._allCircle[index][1];
            options.editable = editable;
            options.center = circle.getCenter();
            options.radius = circle.getRadius();
            circle.setOptions(options);
        };
        // ****方法:取得Circle Editable屬性*****
        EupMap.prototype.GetCircleEditable = function (index) {
            var circle = this._allCircle[index][0];
            return circle.getEditable() || false;
        };
        // ****方法:取得Circle 中心點屬性****
        EupMap.prototype.GetCircleCenter = function (index) {
            var circle = this._allCircle[index][0];
            return circle.getCenter().lng() + ',' + circle.getCenter().lat();
        };
        // ****方法:取得Circle Radius屬性****
        EupMap.prototype.GetCircleRadius = function (index) {
            var circle = this._allCircle[index][0];
            return circle.getRadius();
        };

        // ****方法:移除全部Labels *****
        EupMap.prototype.RemoveAllTargetLabels = function () {
            for (var i = 0; i < this._allTargetLabels.length; i++)
                this._allTargetLabels[i].setMap(null);
            this._allTargetLabels.splice(0, this._allTargetLabels.length);
        };

        //新增Target label
        EupMap.prototype.AddTargetLabel = function (latLng, name) {
            //return this._map;
            var _Labels = new MapLabel({
                text: name,
                fontColor: 'blue',
                position: latLng,
                map: this._map,
                fontSize: 10,
                align: 'center',
                maxZoom: 20,
                minZoom: 8
            });
            return this._allTargetLabels.push(_Labels) - 1;
        };

        // ****方法:移除全部Labels *****
        EupMap.prototype.RemoveAllLabels = function () {
            for (var i = 0; i < this._allLabels.length; i++)
                this._allLabels[i].setMap(null);
            this._allLabels.splice(0, this._allLabels.length);
        };

        /*for地標使用(start) add by yorick 20160322*/
        // ****方法:設定Target Label Visible*****
        EupMap.prototype.SetTargetLabelIsVisible = function (index, isVisible) {
            try {
                var overlay = this._allTargetLabels[index];
                overlay.a.hidden = !isVisible;
            } catch (e) {
                console.log(e.message);
            }
        };

        // ****方法:設定Target Label All Visible *****
        EupMap.prototype.SetAllTargetLabelVisible = function (isVisible) {
            for (var i = 0; i < this._allTargetLabels.length; i++)
                this.SetTargetLabelIsVisible(i, isVisible);
        };

        // ****方法:加入LocTarget,回傳:加入後的index *****
        EupMap.prototype.AddTargetMarker = function (gisX, gisY, img, title, zIndex, anchorX, anchorY) {
            var options = {
                map: this._map,
                position: new google.maps.LatLng(gisY, gisX),
                icon: {
                    url: 'img/Default.png'
                }
            };
            if (title != undefined) options.title = title;
            if (zIndex != undefined) options.zIndex = zIndex;
            if (img != undefined) options.icon.url = img;
            if (anchorX != undefined && anchorY != undefined)
                options.icon.anchor = new google.maps.Point(anchorX, anchorY);
            var marker = new google.maps.Marker(options);
            return this._allTargetMarkers.push(marker) - 1;
        };

        // ****方法:加入LocTarget,回傳:加入後的index *****
        EupMap.prototype.AddTargetMarker_Icon = function (gisX, gisY, Icon, title, zIndex, anchorX, anchorY) {
            var options = {
                map: this._map,
                position: new google.maps.LatLng(gisY, gisX),
                icon: Icon
            };
            if (title != undefined) options.title = title;
            if (zIndex != undefined) options.zIndex = zIndex;
            if (anchorX != undefined && anchorY != undefined)
                options.icon.anchor = new google.maps.Point(anchorX, anchorY);
            var marker = new google.maps.Marker(options);
            return this._allTargetMarkers.push(marker) - 1;
        };

        // ****方法:移除全部LocTarget *****
        EupMap.prototype.RemoveAllTargetMarkers = function () {
            for (var i = 0; i < this._allTargetMarkers.length; i++)
                this._allTargetMarkers[i].setMap(null);
            this._allTargetMarkers.splice(0, this._allTargetMarkers.length);
        };

        // ****方法:設定LocTarget all 屬性*****
        EupMap.prototype.SetTargetMarker = function (markerIndex, clickable, cursor, icon_url, icon_anchorX, icon_anchorY, gisX, gisY, title, visible, zIndex, draggable) {
            var mk = this._allTargetMarkers[markerIndex];
            var opt = {};
            if (clickable != undefined) opt.clickable = clickable;
            if (cursor != undefined) opt.cursor = cursor;
            opt.icon = mk.getIcon();
            if (icon_url != undefined) opt.icon.url = icon_url;
            if (icon_anchorX != undefined && icon_anchorY != undefined)
                opt.icon.anchor = new google.maps.Point(icon_anchorX, icon_anchorY);
            if (gisX != undefined && gisY != undefined) opt.position = new google.maps.LatLng(gisY, gisX);
            if (title != undefined) opt.title = title;
            if (visible != undefined) opt.visible = visible;
            if (zIndex != undefined) opt.zIndex = zIndex;
            if (draggable != undefined) opt.draggable = draggable;
            mk.setOptions(opt);
        };

        // ****方法:取得目前LocTarget總數量 *****
        EupMap.prototype.GetAllTargetMarkersLenth = function () {
            return this._allTargetMarkers.length;
        };

        // ****方法:設定LocTargetMarker Visible *****
        EupMap.prototype.SetTargetMarkerVisible = function (markerIndex, isVisible) {
            this._allTargetMarkers[markerIndex].setVisible(isVisible);
        };

        // ****方法:取得LocTargetMarker Visible *****
        EupMap.prototype.GetTargetMarkerVisible = function (markerIndex) {
            return this._allTargetMarkers[markerIndex].visible;
        };

        // ****方法:設定LocTargetMarker All Visible *****
        EupMap.prototype.SetAllTargetMarkerVisible = function (isVisible) {
            for (var i = 0; i < this._allTargetMarkers.length; i++)
                this._allTargetMarkers[i].setVisible(isVisible);
        };




        //回傳所增加的地標
        EupMap.prototype.GetAddMarker = function (vLog_GISY, vLog_GISX, title, StatusImagePath) {
            var marker = new google.maps.Marker({
                position: new google.maps.LatLng(vLog_GISY,
                    vLog_GISX),
                map: this._map,
                title: title,
                icon: StatusImagePath
            });

            //
            if (this.IsEnabledLocateInfo == true)
                this.AddDblClickEventOnMarkerForLocate(marker); //.

            return marker;
        };

        //回傳所增加的Label
        EupMap.prototype.GetAddLabel = function (vLog_GISY, vLog_GISX, text) {
            var _mapLabel = new MapLabel({
                text: text,
                position: new google.maps.LatLng(vLog_GISY,
                    vLog_GISX),
                map: this._map,
                fontSize: 10,
                fontColor: 'blue',
                align: 'center',
                maxZoom: 20,
                minZoom: 14,
                fillOpacity: 1,
                zIndex: 1000
            });

            //  _mapLabel.set('position', new google.maps.LatLng(vLog_GISY,
            //   vLog_GISX));
            return _mapLabel;
        };

        //回傳所增加的工地
        EupMap.prototype.GetAddPolygon = function (p_pathsArryStr, p_strokeColor, p_strokeOpacity, p_strokeWeight, p_fillColor, p_fillOpacity, p_zIndex) {
            
            var options = {
                paths: p_pathsArryStr,
                strokeColor: p_strokeColor,
                strokeOpacity: p_strokeOpacity,
                strokeWeight: p_strokeWeight,
                fillColor: p_fillColor,
                fillOpacity: p_fillOpacity,
                zIndex: p_zIndex,
                map: this._map
            };
            var newPolygon = new google.maps.Polygon(options);
            this._allPolygon.push([newPolygon, options])
            return newPolygon;
        };

        //#region Click地標 顯示地標資訊 相關事件

        // ****方法 Marker Click地標 顯示地標資訊 事件 *****
        EupMap.prototype.AddClickEventOnMarkerForShowLandmarkInfo = function (marker, landmarkData) {
            var _this = this;

            google.maps.event.addListener(marker, 'click', function (event) {
                if (_this.LandmarkInfowindow != null)
                    _this.HideLandmarkInfowindow();

                //設定infowindow內容
                var content = "<b>" + landmarkData.LT_Name + "</b></br>" +
                    ("LandmarkNo") + "：" + landmarkData.LT_CustNo + "</br>" +
                    ("LandmarkDesc") + "：" + landmarkData.LT_Desc + "</br>" +
                    ("LandmarkTel") + "：" + landmarkData.LT_Phone + "</br>" +
                    "<hr>" +
                    ("LandmarkCreateCompany") + "：" + landmarkData.Cust_Name;

                _this.ShowLandmarkInfowindow(marker, content);
            });
        };

        //#endregion
        /*ThiemNN */
        EupMap.prototype.AddDragendEventOnPointMarker = function (markerIndex, callBack) {
            var me = this;
            var currentMarker = this._pointMarkers[markerIndex];
            google.maps.event.addListener(currentMarker, 'dragend', function (event) {
                var cursor_gisXY = me._GetGisXYFromViewPixelXY(me._cursorX, me._cursorY); //for cursor
                var point = me._GetScreenPoint_FromMapLeftTop(event.latLng.lng(), event.latLng.lat()); //for marker
                var idx = me._FindPointMarkerIndex(currentMarker);
                if (idx >= 0) callBack(idx, event.latLng.lng(), event.latLng.lat(), point.x, point.y, cursor_gisXY.lng(), cursor_gisXY.lat(), me._cursorX, me._cursorY);
            });
        };

        EupMap.prototype.AddClickEventOnPointMarker = function (markerIndex, callBack) {
            var me = this;
            var currentMarker = this._pointMarkers[markerIndex];
            google.maps.event.addListener(currentMarker, 'click', function (event) {
                var cursor_gisXY = me._GetGisXYFromViewPixelXY(me._cursorX, me._cursorY); // ** for cursor **
                var point = me._GetScreenPoint_FromMapLeftTop(event.latLng.lng(), event.latLng.lat()); // ** for marker **
                var idx = me._FindPointMarkerIndex(currentMarker);
                if (idx >= 0) callBack(idx, event.latLng.lng(), event.latLng.lat(), point.x, point.y, cursor_gisXY.lng(), cursor_gisXY.lat(), me._cursorX, me._cursorY);
            });
        };

        EupMap.prototype.AddRightClickEventOnPointMarker = function (markerIndex, callBack) {
            var me = this;
            var currentMarker = this._pointMarkers[markerIndex];
            google.maps.event.addListener(currentMarker, 'rightclick', function (event) {
                var cursor_gisXY = me._GetGisXYFromViewPixelXY(me._cursorX, me._cursorY); // ** for cursor **
                var point = me._GetScreenPoint_FromMapLeftTop(event.latLng.lng(), event.latLng.lat()); // ** for marker **
                var idx = me._FindPointMarkerIndex(currentMarker);
                if (idx >= 0) callBack(idx, event.latLng.lng(), event.latLng.lat(), point.x, point.y, cursor_gisXY.lng(), cursor_gisXY.lat(), me._cursorX, me._cursorY);
            });
        };

        EupMap.prototype.AddDragendEventOnStopMarker = function (markerIndex, callBack) {
            var me = this;
            var currentMarker = this._stopMarkers[markerIndex];
            google.maps.event.addListener(currentMarker, 'dragend', function (event) {
                var cursor_gisXY = me._GetGisXYFromViewPixelXY(me._cursorX, me._cursorY); //for cursor
                var point = me._GetScreenPoint_FromMapLeftTop(event.latLng.lng(), event.latLng.lat()); //for marker
                var idx = me._FindStopMarkerIndex(currentMarker);
                if (idx >= 0) callBack(idx, event.latLng.lng(), event.latLng.lat(), point.x, point.y, cursor_gisXY.lng(), cursor_gisXY.lat(), me._cursorX, me._cursorY);
            });
        };

        EupMap.prototype.AddClickEventOnStopMarker = function (markerIndex, callBack) {
            var me = this;
            var currentMarker = this._stopMarkers[markerIndex];
            google.maps.event.addListener(currentMarker, 'click', function (event) {
                var cursor_gisXY = me._GetGisXYFromViewPixelXY(me._cursorX, me._cursorY); // ** for cursor **
                var point = me._GetScreenPoint_FromMapLeftTop(event.latLng.lng(), event.latLng.lat()); // ** for marker **
                var idx = me._FindStopMarkerIndex(currentMarker);
                if (idx >= 0) callBack(idx, event.latLng.lng(), event.latLng.lat(), point.x, point.y, cursor_gisXY.lng(), cursor_gisXY.lat(), me._cursorX, me._cursorY);
            });
        };
        EupMap.prototype.AddRightClickEventOnStopMarker = function (markerIndex, callBack) {
            var me = this;
            var currentMarker = this._stopMarkers[markerIndex];
            google.maps.event.addListener(currentMarker, 'rightclick', function (event) {
                var cursor_gisXY = me._GetGisXYFromViewPixelXY(me._cursorX, me._cursorY); // ** for cursor **
                var point = me._GetScreenPoint_FromMapLeftTop(event.latLng.lng(), event.latLng.lat()); // ** for marker **
                var idx = me._FindStopMarkerIndex(currentMarker);
                if (idx >= 0) callBack(idx, event.latLng.lng(), event.latLng.lat(), point.x, point.y, cursor_gisXY.lng(), cursor_gisXY.lat(), me._cursorX, me._cursorY);
            });
        };

        EupMap.prototype.AddClickEventOnMarkerForCarInfo = function (GISX, GISY, marker, carUnicode) {
            var _this = this;
            google.maps.event.addListener(marker, 'click', function (event) {
                _this._car_Unicode = carUnicode;
                Section_Spy_Eup.now_mapTodayInfoClosed = false;
                Section_Spy_Eup.drawCarTodayLabel(GISX, GISY, carUnicode);
                Section_Spy_Eup.ShowCarJourney(carUnicode);
            });
        };

        EupMap.prototype.ShowFlightPath = function (flightPlanCoordinates) {
            this.RemoveFlightPath();

            flightPath = new google.maps.Polyline({
                path: flightPlanCoordinates,
                geodesic: true,
                strokeColor: '#CC0066',
                strokeOpacity: 1.0,
                strokeWeight: 4
            });

            flightPath.setMap(this._map);
        };

        EupMap.prototype.RemoveFlightPath = function () {
            flightPath.setMap(null);
        }
        /*End ThiemNN */

        // ****方法 Polygon Click工地 顯示工地資訊 事件 *****
        EupMap.prototype.AddClickEventOnPolygonForShowSelfSiteInfo = function (GISY, GISX, polygon, SelfSiteData) {
            var _this = this;

            google.maps.event.addListener(polygon, 'click', function (event) {
                var Cust_Name = "";

                if (!$("div.resize_right_zone div.pop_right_zone").hasClass('hide_this'))
                    $("section.section_spy #btn_CloseEdit").click();
                if (_this.SelfSiteInfowindow != null)
                    _this.HideSelfSiteInfowindow();
                New_SelfSite_Component.NowSelfSiteData = SelfSiteData;
                if (SelfSiteData.F_Cust_ID == Section_Spy_Eup._AccountInfo.Cust_ID)
                    Cust_Name = Section_Spy_Eup._AccountInfo.Cust_Name;
                else if (Section_Spy_Eup.AllSubCustInfo != "") {
                    var SubCustInfo = $.grep(Section_Spy_Eup.AllSubCustInfo, function (e) { return e.Cust_ID == SelfSiteData.F_Cust_ID });
                    Cust_Name = ((typeof SubCustInfo[0].Cust_Name === "undefined") ? "" : SubCustInfo[0].Cust_Name);
                }
                //設定infowindow內容
                var content = "<b>" + SelfSiteData.F_AreaName + "</b></br>" +
                    ("SiteDesc") + "：" + SelfSiteData.F_AreaDesc + "</br>" +
                    ("PersonIncharge") + "：" + SelfSiteData.F_InCharge + "</br>" +
                    "<hr>" +
                    ("LandmarkCreateCompany") + "：" + Cust_Name;
                if (SelfSiteData.F_Cust_ID == Section_Spy_Eup._AccountInfo.Cust_ID) {
                    if (New_SelfSite_Component.NowSelfSiteData.F_Shape == 1)
                        content += "<a polygon-ind='" + _this._FindCircleIndex(polygon) + "' class='site_edit'>" + ('Edit') + "</a>";
                    else
                        content += "<a polygon-ind='" + _this._FindPolygonIndex(polygon) + "' class='site_edit'>" + ('Edit') + "</a>";
                }
                _this.MoveCenterTo(GISX, GISY);
                _this.ShowSelfSiteInfowindow(GISY, GISX, polygon, content);
            });
        };

        //#region DBClick地圖、Marker 顯示 定位資訊框 相關事件

        // ****方法 Marker dblclick 出現定位資訊 事件 *****
        EupMap.prototype.AddDblClickEventOnMarkerForLocate = function (marker) {
            var _this = this;

            google.maps.event.addListener(marker, 'dblclick', function (event) {
                if (_this.LccateStreetView != null)
                    _this.HideLocateMarkerInfo();

                _this.ShowLocateMarkerInfo(marker.getPosition().lat(), marker.getPosition().lng(), false);
            });
        };

        // ****方法:加入Map dblclick 出現定位資訊 事件 *****
        EupMap.prototype.AddDbClickEventOnMapForLocate = function () {
            var _this = this;

            google.maps.event.addListener(this._map, 'dblclick', function (event) {
                if (_this.IsEnabledLocateInfo == false)
                    return;

                if (_this.LccateStreetView != null)
                    _this.HideLocateMarkerInfo();

                _this.ShowLocateMarkerInfo(event.latLng.lat(), event.latLng.lng(), true);
            });
        };

        //#endregion
        /* ThiemNN */
        EupMap.prototype.ShowCarInfowindow = function (marker, content) {
            var _this = this;
            this.CarInfowindow = new google.maps.InfoWindow({
                content: content,
                disableAutoPan: true,
                zIndex: 6000
            });
            this.CarInfowindow.open(this._map, marker);
            /* ThiemNN */
            google.maps.event.addListener(this.CarInfowindow, 'closeclick', function () {
                _this._car_Unicode = null;
            });
            /* End ThiemNN*/
        }

        EupMap.prototype.HideCarInfowindow = function () {
            if (this.CarInfowindow == null)
                return;

            this.CarInfowindow.close();
            this.CarInfowindow = null;
        }
        /* End ThiemNN*/
        //#region 顯示 地標資訊 InfoWindow 相關方法


        EupMap.prototype.ShowLandmarkInfowindow = function (marker, content) {
            if (this.LandmarkInfowindow != null)
                this.HideLandmarkInfowindow();

            this.LandmarkInfowindow = new google.maps.InfoWindow({
                content: content,
                disableAutoPan: true,
                zIndex: 99
            });

            this.LandmarkInfowindow.open(this._map, marker);
        }


        EupMap.prototype.HideLandmarkInfowindow = function () {
            if (this.LandmarkInfowindow == null)
                return;

            this.LandmarkInfowindow.close();
            this.LandmarkInfowindow = null;
        }

        //#endregion

        //#region 顯示 工地資訊 InfoWindow 相關方法
        EupMap.prototype.ShowSelfSiteInfowindow = function (GISY, GISX, polygon, content) {
            this.SelfSiteInfowindow = new google.maps.InfoWindow({
                content: content,
                position: { lat: GISY, lng: GISX },
                disableAutoPan: true,
                zIndex: 9999
            });

            this.SelfSiteInfowindow.open(this._map, polygon);
        }

        EupMap.prototype.HideSelfSiteInfowindow = function () {
            if (this.SelfSiteInfowindow == null)
                return;

            this.SelfSiteInfowindow.close();
            this.SelfSiteInfowindow = null;
        }

        //#endregion        

        //#region 顯示 定位資訊框 相關方法

        //顯示定位資訊 (經緯度 to 地址)
        EupMap.prototype.ShowLocateMarkerInfo = function (lat, lng, isShowMarker) {
            var _this = this;
            var geocoder = new google.maps.Geocoder;
            var latlng = {
                lat: lat,
                lng: lng
            };

            //根據座標做地址定位
            geocoder.geocode({
                'location': latlng
            }, function (results, status) {
                if (status === google.maps.GeocoderStatus.OK) {
                    if (results[0]) {
                        if (isShowMarker == true)
                            _this.LocateMarkerIndex = _this.AddMarker(lng, lat, getMarkerImage('25'));

                        //#region 顯示 定位資訊框

                        var address = results[0].formatted_address;
                        var content = address + "</br>" + lat.toFixed(6) + " , " + lng.toFixed(6);
                        var centerControl = new LocateInfoControl(content, lat, lng);
                        _this._map.controls[google.maps.ControlPosition.BOTTOM_CENTER].push(centerControl);

                        //#endregion
                    }
                }
            });

            //定位資訊框 控制項
            function LocateInfoControl(content, lat, lng) {
                var controlDiv = document.createElement('div');
                controlDiv.index = 1;
                var table = document.createElement('table');
                table.style.width = '100%';
                var tr = document.createElement('tr');

                var controlUI = document.createElement('div');
                controlUI.style.backgroundColor = '#fff';
                controlUI.style.border = '2px solid #fff';
                controlUI.style.borderRadius = '3px';
                controlUI.style.boxShadow = '0 2px 6px rgba(0,0,0,.3)';
                controlUI.style.cursor = 'pointer';
                // controlUI.style.marginBottom = '30px';
                controlUI.style.textAlign = 'center';

                var margin_bot = $("#map_btm_text").length > 0 ? $("#map_btm_text").height() : 30;
                controlUI.style.marginBottom = margin_bot + 'px';

                //#region 街景控制項

                var street_view_ui = document.createElement('div');
                street_view_ui.id = "street_view_ui";
                street_view_ui.style.height = "80px";
                street_view_ui.style.width = "130px";

                _this.LccateStreetView = new google.maps.StreetViewPanorama(
                    street_view_ui, {
                        position: {
                            lat: lat,
                            lng: lng
                        },
                        pov: {
                            heading: 34,
                            pitch: 10
                        },
                        disableDefaultUI: true
                    });

                var td1 = document.createElement('td');
                td1.appendChild(street_view_ui);
                tr.appendChild(td1);

                //#region 因為街景物件初始化會有時間差，故稍等一下再動作

                setTimeout(function () {
                    if (_this.LccateStreetView != null &&
                        _this.LccateStreetView.getStatus() == google.maps.StreetViewStatus.OK)
                        _this.LccateStreetView.setVisible(true); //要加這行才可避免街景遺失
                    else {
                        street_view_ui.innerHTML = "無街景資料";
                        street_view_ui.style.textAlign = "center";
                        street_view_ui.style.color = 'rgb(25,25,25)';
                        street_view_ui.style.fontFamily = 'Roboto,Arial,sans-serif';
                        street_view_ui.style.fontSize = '14px';
                        street_view_ui.style.lineHeight = '80px';
                        street_view_ui.style.paddingLeft = '5px';
                        street_view_ui.style.paddingRight = '5px';
                    }

                    $(".gm-style-cc").hide();
                }, 500);

                //#endregion

                //#endregion

                //#region 地址資訊

                var controlText = document.createElement('div');
                controlText.style.color = 'rgb(25,25,25)';
                controlText.style.fontFamily = 'Roboto,Arial,sans-serif';
                controlText.style.fontSize = '14px';
                controlText.style.lineHeight = '38px';
                controlText.style.paddingLeft = '5px';
                controlText.style.paddingRight = '5px';
                controlText.innerHTML = content;
                var td2 = document.createElement('td');
                td2.appendChild(controlText);
                tr.appendChild(td2);

                //#endregion

                table.appendChild(tr);
                controlUI.appendChild(table);
                controlDiv.appendChild(controlUI);

                return controlDiv;
            }
        }

        //移除定位資訊
        EupMap.prototype.HideLocateMarkerInfo = function () {
            if (this.LccateStreetView == null)
                return;

            //移除 Marker
            if (this.LocateMarkerIndex != null) {
                this.RemoveMarker(this.LocateMarkerIndex);
                this.LocateMarkerIndex = null;
            }

            //隱藏 定位資訊框
            this._map.controls[google.maps.ControlPosition.BOTTOM_CENTER].clear();

            //清除街景
            this.LccateStreetView = null;
        }

        //#endregion

        //#region 顯示 經緯度資訊框 相關方法 20161129

        //顯示座標資訊
        EupMap.prototype.ShowCoordinateInfo = function (lat, lng) {
            var _this = this;

            var content = ("Lng") + "：" + lng.toFixed(6) + " ； " +
                ("Lat") + "：" + lat.toFixed(6);

            //若已經有建立過 則只要更新內容即可
            if ($("#div_coordinate_info").length > 0) {
                //更新內容
                $("#div_coordinate_info").text(content);

                //更新位置
                var margin_bot = $(".map_btm_text").length > 0 ? $(".map_btm_text").height() : 0;
                $("#div_coordinate_info_ui").css("marginBottom", margin_bot + 'px');

                return;
            }

            var centerControl = new CoordinateInfoControl(content, lat, lng);
            _this._map.controls[google.maps.ControlPosition.BOTTOM_LEFT].push(centerControl);


            //座標資訊框 控制項
            function CoordinateInfoControl(content) {
                var controlDiv = document.createElement('div');
                controlDiv.index = 1;

                var controlUI = document.createElement('div');
                controlUI.id = "div_coordinate_info_ui";
                controlUI.style.backgroundColor = '#fff';
                controlUI.style.border = '2px solid #fff';
                controlUI.style.borderRadius = '3px';
                controlUI.style.boxShadow = '0 2px 6px rgba(0,0,0,.3)';
                controlUI.style.cursor = 'pointer';
                controlUI.style.textAlign = 'center';

                var margin_bot = $(".map_btm_text").length > 0 ? $(".map_btm_text").height() + 3 : 0;
                controlUI.style.marginBottom = margin_bot + 'px';

                //修正位置
                controlUI.style.marginLeft = "-40px";

                var controlText = document.createElement('div');
                controlText.id = "div_coordinate_info"
                controlText.style.color = 'rgb(25,25,25)';
                controlText.style.fontFamily = 'Roboto,Arial,sans-serif';
                controlText.style.fontSize = '12px';
                controlText.style.lineHeight = '25px';
                controlText.style.paddingLeft = '5px';
                controlText.style.paddingRight = '5px';
                controlText.style.display = 'none';
                controlText.innerHTML = content;

                controlUI.appendChild(controlText);
                controlDiv.appendChild(controlUI);

                return controlDiv;
            }
        }

        //移除座標資訊框
        EupMap.prototype.HideCoordinateInfo = function () {
            if ($("#div_coordinate_info").length == 0)
                return;

            this._map.controls[google.maps.ControlPosition.BOTTOM_LEFT].clear();
        }

        //#endregion

        /*for地標使用(end) add by yorick 20160322*/

        EupMap._initialized = true;
    }

    //掛載 DBClick地圖 出現定位資訊 事件
    if (this.IsEnabledLocateInfo == true)
        this.AddDbClickEventOnMapForLocate();
}
// ****方法:設定地圖下方車輛資訊 單地圖*****
function SetOneMapBottomInput(mapid, InfoToStr, ischangerow) {
    var divmap_btm_text = document.getElementById('map_btm_text_' + mapid);
    if (divmap_btm_text) {
        divmap_btm_text.parentNode.removeChild(divmap_btm_text);
    }

    var divBtmTxt = document.createElement('div');
    divBtmTxt.id = 'map_btm_text_' + mapid;
    divBtmTxt.style.position = "absolute";
    divBtmTxt.style.width = '100%';
    //divBtmTxt.style.height = '5%';
    if (ischangerow) {
        // divBtmTxt.style.height = '5.1rem';
    } else {
        // divBtmTxt.style.height = '1.7rem';
    }
    divBtmTxt.style.bottom = '0';
    // divBtmTxt.style.bottom = 0;
    divBtmTxt.style.zIndex = 10;
    divBtmTxt.style.overflow = "hidden";
    divBtmTxt.style.backgroundColor = 'white';
    divBtmTxt.style.wordBreak = "break-all";
    divBtmTxt.innerHTML = InfoToStr;

    divBtmTxt.className += " map_btm_text";//加入此className 當作識別

    var objTo = document.getElementById(mapid);
    objTo.appendChild(divBtmTxt);
}

// Harvey - new marker label
function Label(opt_options) {
    // Initialization
    this.setValues(opt_options);

    // Label specific
    var span = this.span_ = document.createElement('span');

    span.style.cssText = 'display: inline-block; ' +
        'background: #4A4A4A \9; ' +
        'filter: alpha(opacity=80) \9; ' +
        'background-color: rgba(74, 74, 74, 0.8); ' +
        'color: white ' +
        'font-weight: bold; ' +
        'border-radius: 5px; ' +
        'font-size: 0.8rem; ' +
        'padding: 4px 5px; ' +
        'text-align: left; ' +
        'margin: 0 auto; ' ;
        //'min-width: 120px';

    var div = this.div_ = document.createElement('div');
    div.setAttribute("class", "arrow" );
    div.appendChild(span);
    div.style.cssText = 'position: absolute; ' +
        'left: 45%; ' +
        'top: 30%; ' +
        '-webkit-transform: translate(-50%, -50%); ' +
        '-moz-transform: translate(-50%, -50%); ' +
        '-ms-transform: translate(-50%, -50%); ' +
        '-o-transform: translate(-50%, -50%); ' +
        'transform: translate(-50%, -50%); ' +
        'text-align: left; ' +
        'width: auto';
    console.log(div)
};
Label.prototype = new google.maps.OverlayView;

// Implement onAdd
Label.prototype.onAdd = function () {
    var pane = this.getPanes().overlayLayer;
    pane.appendChild(this.div_);

    // Ensures the label is redrawn if the text or position is changed.
    var me = this;
    this.listeners_ = [
        google.maps.event.addListener(this, 'position_changed',
            function () { me.draw(); }),
        google.maps.event.addListener(this, 'text_changed',
            function () { me.draw(); })
    ];
};

// Implement onRemove
Label.prototype.onRemove = function () {
    if (this.div_ !== undefined && this.div_ !== null) {
        this.div_.parentNode.removeChild(this.div_);

        // Label is removed from the map, stop updating its position/text.
        for (var i = 0, I = this.listeners_.length; i < I; ++i) {
            google.maps.event.removeListener(this.listeners_[i]);
        }
    }
};

// Implement draw
Label.prototype.draw = function () {
    var projection = this.getProjection();
    var position = projection.fromLatLngToDivPixel(this.get('position'));

    var div = this.div_;
    div.style.left = position.x - 5 + 'px';
    div.style.top = position.y - 45 + 'px';
    div.style.display = 'block';
    div.style.zIndex = 6;

    this.span_.innerHTML = this.get('text').toString();
};
// End Harvey
