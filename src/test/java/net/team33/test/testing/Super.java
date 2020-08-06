package net.team33.test.testing;

import java.util.*;

import static java.util.Collections.*;

public abstract class Super<R extends Super<R>> {

  private int theSuperPrimitive;
  private String theSuperString;
  private Number theSuperNumber;
  private byte[] theSuperByteArray = {};
  private Object theSuperObject;
  private List<Object> theSuperList;
  private Set<Object> theSuperSet;
  private Map<Object, Object> theSuperMap;
  
  protected abstract R getThis();

  public final int getTheSuperPrimitive() {
    return theSuperPrimitive;
  }

  public final R setTheSuperPrimitive(final int theSuperPrimitive) {
    this.theSuperPrimitive = theSuperPrimitive;
    return getThis();
  }

  public final String getTheSuperString() {
    return theSuperString;
  }

  public final R setTheSuperString(final String theSuperString) {
    this.theSuperString = theSuperString;
    return getThis();
  }

  public final Number getTheSuperNumber() {
    return theSuperNumber;
  }

  public final R setTheSuperNumber(final Number theSuperNumber) {
    this.theSuperNumber = theSuperNumber;
    return getThis();
  }

  public final byte[] getTheSuperByteArray() {
    return theSuperByteArray.clone();
  }

  public final R setTheSuperByteArray(final byte[] theSuperByteArray) {
    this.theSuperByteArray = theSuperByteArray.clone();
    return getThis();
  }

  public final Object getTheSuperObject() {
    return theSuperObject;
  }

  public final R setTheSuperObject(final Object theSuperObject) {
    this.theSuperObject = theSuperObject;
    return getThis();
  }

  public final List<Object> getTheSuperList() {
    //noinspection ReturnOfNull
    return (null == theSuperList) ? null : unmodifiableList(theSuperList);
  }

  public final R setTheSuperList(final Collection<?> theSuperList) {
    //noinspection AssignmentToNull
    this.theSuperList = (null == theSuperList) ? null : new ArrayList<>(theSuperList);
    return getThis();
  }

  public final Set<Object> getTheSuperSet() {
    //noinspection ReturnOfNull
    return (null == theSuperSet) ? null : unmodifiableSet(theSuperSet);
  }

  public final R setTheSuperSet(final Collection<?> theSuperSet) {
    //noinspection AssignmentToNull
    this.theSuperSet = (null == theSuperSet) ? null : new HashSet<>(theSuperSet);
    return getThis();
  }

  public final Map<Object, Object> getTheSuperMap() {
    //noinspection ReturnOfNull
    return (null == theSuperMap) ? null : unmodifiableMap(theSuperMap);
  }

  public final R setTheSuperMap(final Map<?, ?> theSuperMap) {
    //noinspection AssignmentToNull
    this.theSuperMap = (null == theSuperMap) ? null : new HashMap<>(theSuperMap);
    return getThis();
  }
}
