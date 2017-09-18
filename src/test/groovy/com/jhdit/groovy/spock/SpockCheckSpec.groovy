package com.jhdit.groovy.spock

import spock.lang.*;

/**
 * Simple tests to confirm Spock test framework functioning correctly (plus useful for quick reference).
 *
 * See https://code.google.com/p/spock/wiki/HelloSpock
 * and https://code.google.com/p/spock/wiki/SpockBasics
 *
 * Latest Spock Jar download: http://mvnrepository.com/artifact/org.spockframework/spock-core/1.0-groovy-2.4
 *
 * Spock blocks:
 *
 * given: preconditions, data fixtures
 * setup: implicit, alias for given
 * when: actions that trigger some outcome
 * then: make assertions about the outcome
 * expect: shorthand for when & then
 * where: applies varied inputs
 * and: subdivides other blocks
 * cleanup: post-conditions, housekeeping
 *
 */

class SpockCheckSpec extends Specification {

    // Fixture methods: use Groovy assert for any checks here
    def setup() {}          // run before every feature method
    def cleanup() {}        // run after every feature method
    def setupSpec() {}     // run before the first feature method
    def cleanupSpec() {}   // run after the last feature method

    def "super simple logic example (basic multiplication)"()  {
        expect:
        3.multiply(7)   == 21
    }

    @Unroll // Run each data row as a separate test, data values can referenced in method name for display
    def "length of Spock's and his friends' names #name"() {
        expect: "matching name length"
        name.size() == length

        where: "character name data"
        name        | length
        "Kirk"      | 4
        "Spock"     | 5
        "Scotty"    | 6
    }

    @Unroll // Run each data row as a separate test, data values can referenced in method name for display
    def "#base raised to power of #exponent is #result"() {
        expect: "a specific outcome"
        Math.pow(base, exponent) == result
        // See http://docs.oracle.com/javase/8/docs/api/java/lang/Math.html#pow-double-double-

        where: "inputs are as follows"
        base | exponent | result
        2 | 0 | 1
        2 | 1 | 2
        2 | 2 | 4
        3 | 2 | 9
        10 | 2 | 100
        2 | 10 | 1024
        2 | -1 | 0.5
    }

    def "exception handling (divide by zero example)" () {
        when: "an exception is raised"
        1 / 0

        then: "the exception is caught & can be verified"
        final ArithmeticException e = thrown()
        e.message == "Division by zero"
    }

    def "stack example: exception handling & multiple when-then blocks"()    {
        setup:
        def stack = new Stack() // Standard Java: java.util.Stack
        def today = new Date()

        expect:
        stack.empty()

        when:
        stack.pop()

        then:
        def e = thrown(EmptyStackException)
        e instanceof EmptyStackException

        when:
        stack.add "First entry"
        stack << [a:1, b:2, c:3] // Map
        stack.add( ['x', 'y', 'z'] ) // List
        stack << today // Last item

        then:
        !stack.empty
        stack.size() == 4
        stack.peek() == today
    }

    @Ignore(value = "TODO") // Convenience annotation to omit tests
    def "ignore for now"() {
        expect:
        true == false
    }

    def "events are published to all subscribers"() {
        def subscriber1 = Mock(Subscriber) // "dynamic" style (mock name inferred from variable name)
        Subscriber subscriber2 = Mock() // "static" style" (mock name & type inferred from variable name & type)
        subscriber2.receive(_) >> {event -> return true } // Set return value

        def publisher = new Publisher()
        publisher.add(subscriber1)
        publisher.add(subscriber2)

        when: "an event is published to all subscribers"
        publisher.fire("event")

        then: "a receive invocation occurs for each listening subscriber"
        1 * subscriber1.receive("event") >> true // Set expected invocations (plus set return value)
        // 1 * subscriber2.receive("event")
    }

    /**
     * A Stub is a fake class that can be programmed with custom behavior.
     */

    def "stubbing (Subscriber)"() {
        Subscriber stub = Stub(Subscriber)
        stub.receive(_) >>> [true, false, true] // Alternate boolean return value

        expect:
        stub.receive "1st event"
        stub.receive("2nd event") == false // Will fail if not false
        stub.receive "3rd event"
    }

}

/*
interface Subscriber  {
    boolean receive(String event)
}
*/

class Subscriber  {
    boolean receive(String event) { println "Recieve ${event}" }
}


class Publisher   {
    List subscribers = []

    void add(Subscriber subscriber)  {
        subscribers.add subscriber
    }

    void fire(String event) {
        subscribers.each()  {
            // Temporarily comment out to see tests fail
            if (!it.receive(event)) {
                throw new RuntimeException("Subscriber problem!")
            }
        }
    }
}
