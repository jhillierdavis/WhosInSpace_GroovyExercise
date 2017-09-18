package com.jhdit.groovy.exercise.whosinspace

class Display {
    private ProcessData processData

    Display(String sourceUrl)   {
        def data =  sourceUrl.toURL().text
        this.processData = new ProcessData(data)
    }

    static void main(String... args) {
        Display display = new Display('http://api.open-notify.org/astros.json')
        display.header()
        display.content()
    }

    private void header()    {
        int crewPadding = processData.maxCrewNameLength
        int craftPadding = processData.maxCraftNameLength

        println "Name".padRight(crewPadding) + " | " + "Craft".padRight(craftPadding)
        println "-".multiply(crewPadding) + " | " + "-".multiply(craftPadding)

    }

    private void content()   {
        int crewPadding = processData.maxCrewNameLength
        int craftPadding = processData.maxCraftNameLength

        processData.getCraft().each { craft ->
            processData.getCrewSortedBySurname(craft).each { astronaut ->
                println "${astronaut.padRight(crewPadding)} | ${craft.padRight(craftPadding)}"
            }
        }
    }
}
