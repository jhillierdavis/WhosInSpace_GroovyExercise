package com.jhdit.groovy.exercise.whosinspace

import groovy.json.JsonSlurper
import spock.lang.Specification

class ProcessDataSpec extends Specification {

    private def TEST_JSON_DATA = '''
{
  "message": "success",
  "number": 6,
  "people": [
    {
      "craft": "ISS",
      "name": "Mikhail Kornienko"
    },
    {
      "craft": "ISS",
      "name": "Scott Kelly"
    },
    {
      "craft": "ISS",
      "name": "Oleg Kononenko"
    },
    {
      "craft": "ISS",
      "name": "Kimiya Yui"
    },
    {
      "craft": "ISS",
      "name": "Kjell Lindgren"
    },
    {
      "craft": "ISS",
      "name": "Sergey Volkov"
    }
  ]
}
'''

  private def STAR_WARS_JSON_DATA = '''
{
  "message": "success",
  "number": 4,
  "people": [
    {
      "craft": "Millennium Falcon",
      "name": "Hans Solo"
    },
    {
      "craft": "Millennium Falcon",
      "name": "Chewbacca"
    },
    {
      "craft": "Slave 1",
      "name": "Boba Fett"
    },
    {
      "craft": "X-Wing Fighter",
      "name": "Luke Skywalker"
    }
  ]
}
'''


  def "process test json"()  {
    given: "test JSON data"
          def data = new JsonSlurper().parseText TEST_JSON_DATA

    expect: "attributes are as expected"
          data.message == "success"
          data.number == 6
          data.people.size == 6
          data.people.first().craft  == "ISS"
          data.people.first().name  == "Mikhail Kornienko"
          // println data.people.first().craft

          data.people.collect { it.name.size() }.max() == "Mikhail Kornienko".size()

          data.people.each() {
              println "${it}"
          }
  }

  /*
  def "longest astronaut name"()  {
    given: "test JSON data"
      def data = new JsonSlurper().parseText TEST_JSON_DATA

    when:
       int longest = data.people.collect { it.name.size() }.max()

    then:
        longest == "Mikhail Kornienko".size()
  }
  */

  def "longest astronaut name"()  {
    given: "test JSON data"
      def processData = new ProcessData( TEST_JSON_DATA )

    when:
    int longest = processData.maxCrewNameLength

    then:
      longest == "Mikhail Kornienko".size()
  }

/*
  def "longest craft name"()  {
    given: "test JSON data"
      def data = new JsonSlurper().parseText TEST_JSON_DATA

    when:
      int longest = data.people.collect { it.craft.size() }.max()

    then:
      longest == "ISS".size()
  }
*/

  def "longest craft name"()  {
    given: "test JSON data"
      def processData = new ProcessData( TEST_JSON_DATA )

    when:
      int longest = processData.maxCraftNameLength

    then:
      longest == "ISS".size()
  }


  def "sort by astronaut surname"() {
    given: "test JSON data"
      def data = new JsonSlurper().parseText TEST_JSON_DATA

    when:
      Closure closureGetSurname =  { it.split(' ').last() }
      def sortedAstronauts = data.people.collect { it.name }.sort ( closureGetSurname )

    then:
      sortedAstronauts.first() == "Scott Kelly"
  }

  def "get crew by craft"() {
    given:
    def processData = new ProcessData( STAR_WARS_JSON_DATA )

    when:
      def crew = processData.getCrew("Millennium Falcon");

    then:
      crew.size() == 2
      crew.contains "Hans Solo"
      crew.contains "Chewbacca"
  }

  def "get crew by craft, sorted by surname"() {
    given:
    def processData = new ProcessData( STAR_WARS_JSON_DATA )

    when:
    def crew = processData.getCrewSortedBySurname("Millennium Falcon");

    then:
    crew.size() == 2
    crew.first() == "Chewbacca"
    crew.last() == "Hans Solo"
  }

}
