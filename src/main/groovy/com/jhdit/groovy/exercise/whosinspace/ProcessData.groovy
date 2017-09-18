package com.jhdit.groovy.exercise.whosinspace

import groovy.json.JsonSlurper

@groovy.transform.EqualsAndHashCode
@groovy.transform.ToString
class ProcessData {
    private int maxCrewNameLength
    private int maxCraftNameLength
    private def data

    ProcessData(def inputJsonData)   {
        this.data = new JsonSlurper().parseText inputJsonData

        this.maxCrewNameLength = data.people.collect { it.name.size() }.max()
        this.maxCraftNameLength = data.people.collect { it.craft.size() }.max()
    }

    List<String> getCraft()  {
        return this.data.people.collect{ it.craft }.unique()
    }


    List<String> getCrew(final String craft)  {
        return this.data.people.findAll { it.craft == craft }.collect{ it.name }
    }

    List<String> getCrewSortedBySurname(String craft)   {
        Closure closureGetSurname =  { it.split(' ').last() }

        def crew = this.getCrew(craft)
        return crew.sort( closureGetSurname )
    }
}
