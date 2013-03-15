package de.twenty11.skysail.server.internal.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import de.twenty11.skysail.server.internal.LimitedQueue;

public class LimitedQueueTest {

    private LimitedQueue<String> limitedQueue;

    @Before
    public void setUp() {
        limitedQueue = new LimitedQueue<String>(2);
    }

    @Test
    public void can_add_first_element() {
        limitedQueue.add("one");
        assertThat(limitedQueue.size(), is(equalTo(1)));
        assertThat(limitedQueue.get(0), is(equalTo("one")));
    }

    @Test
    public void can_add_second_element() {
        limitedQueue.add("one");
        limitedQueue.add("two");
        assertThat(limitedQueue.size(), is(equalTo(2)));
        assertThat(limitedQueue.get(1), is(equalTo("two")));
    }

    @Test
    public void removes_first_when_adding_third_element() {
        limitedQueue.add("one");
        limitedQueue.add("two");
        limitedQueue.add("three");
        assertThat(limitedQueue.size(), is(equalTo(2)));
        assertThat(limitedQueue.get(0), is(equalTo("two")));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void add_with_two_parameters_throws_unspportedOperationException() throws Exception {
        limitedQueue.add(1, "test");
    }    

    @Test(expected = UnsupportedOperationException.class)
    public void addAll_with_collection_throws_unspportedOperationException() throws Exception {
        limitedQueue.addAll(Collections.<String> emptyList());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void addAll_with_two_parameters_throws_unspportedOperationException() throws Exception {
        limitedQueue.addAll(1, Collections.<String> emptyList());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void addFirst_throws_unspportedOperationException() throws Exception {
        limitedQueue.addFirst("test");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void addLast_throws_unspportedOperationException() throws Exception {
        limitedQueue.addLast("test");
    }

}
