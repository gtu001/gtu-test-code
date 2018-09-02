# -*- coding: utf-8 -*-
import urllib.parse
import urllib.error
import urllib.request
from retry import retry
import logging
import os
import codecs
import json
from gtu.reflect import checkSelf


@retry(urllib.error.URLError, tries=3, delay=2, backoff=2)
@retry(urllib.error.HTTPError, tries=3, delay=2, backoff=2)
def route_check(routing_service_host, query_string_values):
    
    if not routing_service_host :
        routing_service_host = os.getenv('ROUTING-SERVICE-HOST', "130.198.90.185:30392")
        
    if not query_string_values :
        query_string_values = {"jobId": "xxx","workStationId": "xxx"}    
    
    query_string = urllib.parse.urlencode(query_string_values) 
    
    url = "http://" + routing_service_host + "/mes/edge/routing-service/routecheck/" + "workOrderId"
    
    geturl = url + "?" + query_string
    
    logging.debug("===routing_service_url===")
    logging.debug(geturl)        
    
    response = urllib.request.urlopen(geturl)
    
    reader = codecs.getreader("utf-8")
    
    route_check_response = json.load(reader(response))
    
    logging.debug("===route check response===" + json.dumps(route_check_response))  
     
    return route_check_response
    


if __name__ == '__main__' :
    
    logger = logging.getLogger('spam_application')
    ch = logging.StreamHandler()
    ch.setLevel(logging.DEBUG)
    logger.addHandler(ch)

    route_check(None, None)
    print("done...")