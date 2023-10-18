package com.exacaster.lighter.application.sessions

import com.exacaster.lighter.application.ApplicationInfo
import com.exacaster.lighter.application.ApplicationState
import com.exacaster.lighter.application.sessions.processors.StatementHandler
import com.exacaster.lighter.backend.Backend
import com.exacaster.lighter.storage.ApplicationStorage
import com.exacaster.lighter.storage.StatementStorage
import com.exacaster.lighter.test.InMemoryStorage
import spock.lang.Specification
import spock.lang.Subject

import static com.exacaster.lighter.test.Factories.*

class CreateStatementTest extends Specification {
    ApplicationStorage storage = new InMemoryStorage()
    Backend backend = Mock()
    StatementHandler statementHandler = Mock()

    @Subject
    SessionService service = new SessionService(storage, Mock(StatementStorage), backend, statementHandler)

    def 'on non existing session id returns NoSessionExists'() {
        given:
        def params = newStatement()

        when: "creating statement"
        def result = service.createStatement("sessionId", params)

        then: "returns no session found"
        result instanceof StatementCreationResult.NoSessionExists
    }

    def "on killed session returns SessionInInvalidState"() {
        given:
        def params = newStatement()
        def session = newSession(ApplicationState.KILLED)
        storage.saveApplication(session)
        backend.getInfo(session) >>  Optional.of( new ApplicationInfo(session.state, session.id))

        when: "creating statement"
        def result = service.createStatement(session.id, params)

        then: "returns session in invalid state"
        result instanceof StatementCreationResult.SessionInInvalidState
        ((StatementCreationResult.SessionInInvalidState)result).getInvalidState() == session.state
    }

    def "on non-completed session returns StatementCreated"() {
        given:
        def statementToCreate = newStatement()
        def session = newSession(ApplicationState.STARTING)
        storage.saveApplication(session)
        backend.getInfo(session) >>  Optional.of( new ApplicationInfo(session.state, session.id))
        def statementCreated = statement()
        statementHandler.processStatement(session.id, statementToCreate) >> statementCreated

        when: "creating statement"
        def result = service.createStatement(session.id, statementToCreate)

        then: "returns statement created"
        result instanceof StatementCreationResult.StatementCreated
        ((StatementCreationResult.StatementCreated)result).getStatement() == statementCreated
    }

}
