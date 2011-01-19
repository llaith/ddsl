package com.kjetland.ddsl.javaclient

import org.joda.time.DateTime
import com.kjetland.ddsl._

/**
 * Created by IntelliJ IDEA.
 * User: mortenkjetland
 * Date: 1/17/11
 * Time: 2:39 PM
 * To change this template use File | Settings | File Templates.
 */

class DdslClientJavaWrapper( ddslClient : DdslClient){

  implicit private def sidj2sid( s :ServiceIdJava) = {
    ServiceId(s.environment, s.serviceType, s.name, s.version)
  }

  implicit private def cidj2cid( c : ClientIdJava) = {
    ClientId( c.environment, c.name, c.version, c.ip)
  }

  implicit private def slj2sl( s : ServiceLocationJava) = {
    ServiceLocation(s.url, s.testUrl, s.quality, s.lastUpdated, s.ip)
  }


  def serviceUp( sid : ServiceIdJava, sl:ServiceLocationJava) : Boolean = ddslClient.serviceUp( Service(sid, sl) )
  def serviceDown( sid : ServiceIdJava, sl:ServiceLocationJava) : Boolean = ddslClient.serviceDown( Service(sid, sl) )

  def getServiceLocations(sid : ServiceIdJava, cid : ClientIdJava) : Array[ServiceLocationJava] = {
    val srs = ddslClient.getServiceLocations( ServiceRequest( sid, cid))

    return srs.map( {sl : ServiceLocation =>
      sl2slj(sl)} ).toArray
  }

  def getBestServiceLocation(sid : ServiceIdJava, cid : ClientIdJava) : ServiceLocationJava = {
    val sl = ddslClient.getBestServiceLocation( ServiceRequest(sid, cid) )
    return sl2slj( sl)
  }

  def disconnect() = ddslClient.disconnect



  private def sl2slj(sl : ServiceLocation) : ServiceLocationJava = {
    new ServiceLocationJava(sl.url, sl.testUrl, sl.quality, sl.lastUpdated, sl.ip)
  }




}


//case class ServiceId(environment : String, serviceType : String, name : String, version : String)
class ServiceIdJava(val environment : String, val serviceType : String, val name : String, val version : String){

  def getCC = ServiceId( environment, serviceType, name, version )

  override def toString() = getCC.toString
}

//
//case class ServiceLocation( url : String, testUrl : String, quality : Double, lastUpdated : DateTime, ip : String)
class ServiceLocationJava(val url : String, val testUrl : String, val quality : Double, val lastUpdated : DateTime, val ip : String){

  def getCC = ServiceLocation(url, testUrl, quality, lastUpdated, ip)

  override def toString() = getCC.toString
  

}
//case class ClientId(environment : String, name : String, version : String, ip : String)
class ClientIdJava(val environment : String, val name : String, val version : String, val ip : String){

  def getCC = ClientId(environment, name, version, ip)

  override def toString = getCC.toString
}