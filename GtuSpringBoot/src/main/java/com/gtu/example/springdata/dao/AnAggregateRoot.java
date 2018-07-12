package com.gtu.example.springdata.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

@Profile({ "spring-data" })
public class AnAggregateRoot {

    /**
     * All domain events currently captured by the aggregate.
     */
    // @Getter(onMethod = @__(@DomainEvents)) //
    private transient final List<Object> domainEvents = new ArrayList<Object>();

    @DomainEvents
    public Collection<Object> domainEvents() {
        // … return events you want to get published here
        return domainEvents;
    }

    /**
     * Registers the given event object for publication on a call to a Spring
     * Data repository's save method.
     * 
     * @param event
     *            must not be {@literal null}.
     * @return
     */
    protected <T> T registerEvent(T event) {
        if (event != null) {
            this.domainEvents.add(event);
        }
        return event;
    }

    @AfterDomainEventPublication
    public void callbackMethod() {
        // … potentially clean up domain events list
        this.domainEvents.clear();
    }
}
