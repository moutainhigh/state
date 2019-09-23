/**
* Licensed to the Apache Software Foundation (ASF) under one
* or more contributor license agreements.  See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership.  The ASF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License.  You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.sodanights.state.client;

import java.util.*;
import java.util.Map.Entry;


/**
 * @param <OPERAND> 操作者(参数类)
 * @param <STATE> 状态
 * @param <EVENTTYPE> 事件类型
 * @param <EVENT> 事件
 *
 */
final public class StateMachineFactory
             <OPERAND, STATE extends Enum<STATE>,
              EVENTTYPE extends Enum<EVENTTYPE>, EVENT> {

  /**
   * Transitions链表
   */
  private final TransitionsListNode transitionsListNode;
  /**
   * 拓扑表
   */
  private Map<STATE, Map<EVENTTYPE,
    Transition<OPERAND, STATE, EVENTTYPE, EVENT>>> stateMachineTable;

  /**
   * 默认状态
   */
  private STATE defaultInitialState;

  /**
   * 是否初始化过
   */
  private final boolean optimized;

  /**
   * 构造方法
   * @param defaultInitialState
   */
  public StateMachineFactory(STATE defaultInitialState) {
    this.transitionsListNode = null;
    this.defaultInitialState = defaultInitialState;
    this.optimized = false;
    this.stateMachineTable = null;
  }

  /**
   * 构造方法 主要是addTransition调用 构建链表
   * @param that
   * @param t
   */
  private StateMachineFactory
      (StateMachineFactory<OPERAND, STATE, EVENTTYPE, EVENT> that,
       ApplicableTransition<OPERAND, STATE, EVENTTYPE, EVENT> t) {
    this.defaultInitialState = that.defaultInitialState;
    this.transitionsListNode 
        = new TransitionsListNode(t, that.transitionsListNode);
    this.optimized = false;
    this.stateMachineTable = null;
  }

  /**
   * 构造方法 主要是installTopology 时调用 构建拓扑表
   * @param that
   * @param optimized
   */
  private StateMachineFactory
      (StateMachineFactory<OPERAND, STATE, EVENTTYPE, EVENT> that,
       boolean optimized) {
    this.defaultInitialState = that.defaultInitialState;
    this.transitionsListNode = that.transitionsListNode;
    this.optimized = optimized;
    if (optimized) {
      makeStateMachineTable();
    } else {
      stateMachineTable = null;
    }
  }

  private interface ApplicableTransition
             <OPERAND, STATE extends Enum<STATE>,
              EVENTTYPE extends Enum<EVENTTYPE>, EVENT> {
    void apply(StateMachineFactory<OPERAND, STATE, EVENTTYPE, EVENT> subject);
  }

  private class TransitionsListNode {
    final ApplicableTransition<OPERAND, STATE, EVENTTYPE, EVENT> transition;
    final TransitionsListNode next;

    TransitionsListNode
        (ApplicableTransition<OPERAND, STATE, EVENTTYPE, EVENT> transition,
        TransitionsListNode next) {
      this.transition = transition;
      this.next = next;
    }
  }

  static private class ApplicableSingleOrMultipleTransition
             <OPERAND, STATE extends Enum<STATE>,
              EVENTTYPE extends Enum<EVENTTYPE>, EVENT>
          implements ApplicableTransition<OPERAND, STATE, EVENTTYPE, EVENT> {
    final STATE preState;
    final EVENTTYPE eventType;
    final Transition<OPERAND, STATE, EVENTTYPE, EVENT> transition;

    ApplicableSingleOrMultipleTransition
        (STATE preState, EVENTTYPE eventType,
         Transition<OPERAND, STATE, EVENTTYPE, EVENT> transition) {
      this.preState = preState;
      this.eventType = eventType;
      this.transition = transition;
    }

    @Override
    public void apply
             (StateMachineFactory<OPERAND, STATE, EVENTTYPE, EVENT> subject) {
      Map<EVENTTYPE, Transition<OPERAND, STATE, EVENTTYPE, EVENT>> transitionMap
        = subject.stateMachineTable.get(preState);
      if (transitionMap == null) {
        // I use HashMap here because I would expect most EVENTTYPE's to not
        //  apply out of a particular state, so FSM sizes would be 
        //  quadratic if I use EnumMap's here as I do at the top level.
        transitionMap = new HashMap<EVENTTYPE,
          Transition<OPERAND, STATE, EVENTTYPE, EVENT>>();
        subject.stateMachineTable.put(preState, transitionMap);
      }
      transitionMap.put(eventType, transition);
    }
  }

  /**
   * @return a NEW StateMachineFactory just like {@code this} with the current
   *          transition added as a new legal transition.  This overload
   *          has no hook object.
   *
   *         Note that the returned StateMachineFactory is a distinct
   *         object.
   *
   *         This method is part of the API.
   *
   * @param preState pre-transition state
   * @param postState post-transition state
   * @param eventType stimulus for the transition
   */
  public StateMachineFactory
             <OPERAND, STATE, EVENTTYPE, EVENT>
          addTransition(STATE preState, STATE postState, EVENTTYPE eventType) {
    return addTransition(preState, postState, eventType, null);
  }

  /**
   * @return a NEW StateMachineFactory just like {@code this} with the current
   *          transition added as a new legal transition.  This overload
   *          has no hook object.
   *
   *
   *         Note that the returned StateMachineFactory is a distinct
   *         object.
   *
   *         This method is part of the API.
   *
   * @param preState pre-transition state
   * @param postState post-transition state
   * @param eventTypes List of stimuli for the transitions
   */
  public StateMachineFactory<OPERAND, STATE, EVENTTYPE, EVENT> addTransition(
      STATE preState, STATE postState, Set<EVENTTYPE> eventTypes) {
    return addTransition(preState, postState, eventTypes, null);
  }

  /**
   * @return a NEW StateMachineFactory just like {@code this} with the current
   *          transition added as a new legal transition
   *
   *         Note that the returned StateMachineFactory is a distinct
   *         object.
   *
   *         This method is part of the API.
   *
   * @param preState pre-transition state
   * @param postState post-transition state
   * @param eventTypes List of stimuli for the transitions
   * @param hook transition hook
   */
  public StateMachineFactory<OPERAND, STATE, EVENTTYPE, EVENT> addTransition(
      STATE preState, STATE postState, Set<EVENTTYPE> eventTypes,
      SingleArcTransition<OPERAND, EVENT,STATE> hook) {
    StateMachineFactory<OPERAND, STATE, EVENTTYPE, EVENT> factory = null;
    for (EVENTTYPE event : eventTypes) {
      if (factory == null) {
        factory = addTransition(preState, postState, event, hook);
      } else {
        factory = factory.addTransition(preState, postState, event, hook);
      }
    }
    return factory;
  }

  /**
   * 添加Transition 调用了私有得构造方法 主要完成构建链表操作
   * @param preState
   * @param postState
   * @param eventType
   * @param hook
   * @return
   */
  public StateMachineFactory
             <OPERAND, STATE, EVENTTYPE, EVENT>
          addTransition(STATE preState, STATE postState,
                        EVENTTYPE eventType,
                        SingleArcTransition<OPERAND,EVENT,STATE> hook){
    return new StateMachineFactory<OPERAND, STATE, EVENTTYPE, EVENT>
        (this, new ApplicableSingleOrMultipleTransition<OPERAND, STATE, EVENTTYPE, EVENT>
           (preState, eventType, new SingleInternalArc(postState, hook)));
  }


  public StateMachineFactory
             <OPERAND, STATE, EVENTTYPE, EVENT>
          addTransition(STATE preState, Set<STATE> postStates,
                        EVENTTYPE eventType,
                        MultipleArcTransition<OPERAND, EVENT, STATE> hook){
    return new StateMachineFactory<OPERAND, STATE, EVENTTYPE, EVENT>
        (this,
         new ApplicableSingleOrMultipleTransition<OPERAND, STATE, EVENTTYPE, EVENT>
           (preState, eventType, new MultipleInternalArc(postStates, hook)));
  }


  public StateMachineFactory
             <OPERAND, STATE, EVENTTYPE, EVENT>
          installTopology() {
    return new StateMachineFactory<OPERAND, STATE, EVENTTYPE, EVENT>(this, true);
  }

  /**
   * 状态转移得具体实现
   * @param operand
   * @param oldState
   * @param eventType
   * @param event
   * @return
   * @throws InvalidStateTransitionException
   * 1.根据状态找寻需要执行得动作map
   * 2.根据事件类型找到具体得执行动作(Transition)
   */
  private STATE doTransition
           (OPERAND operand, STATE oldState, EVENTTYPE eventType, EVENT event)
      throws InvalidStateTransitionException {
    Map<EVENTTYPE, Transition<OPERAND, STATE, EVENTTYPE, EVENT>> transitionMap
      = stateMachineTable.get(oldState);
    if (transitionMap != null) {
      Transition<OPERAND, STATE, EVENTTYPE, EVENT> transition
          = transitionMap.get(eventType);
      if (transition != null) {
        return transition.doTransition(operand, oldState, event, eventType);
      }
    }
    throw new InvalidStateTransitionException(oldState, eventType);
  }

  private synchronized void maybeMakeStateMachineTable() {
    if (stateMachineTable == null) {
      makeStateMachineTable();
    }
  }

  /**
   * 构建拓扑表得具体实现
   */
  private void makeStateMachineTable() {

    //Transition 栈
    Stack<ApplicableTransition<OPERAND, STATE, EVENTTYPE, EVENT>> stack =
      new Stack<ApplicableTransition<OPERAND, STATE, EVENTTYPE, EVENT>>();

    //初始化 状态,Transition Map
    Map<STATE, Map<EVENTTYPE, Transition<OPERAND, STATE, EVENTTYPE, EVENT>>>
      prototype = new HashMap<STATE, Map<EVENTTYPE, Transition<OPERAND, STATE, EVENTTYPE, EVENT>>>();

    //默认状态
    prototype.put(defaultInitialState, null);

    stateMachineTable
       = new EnumMap<STATE, Map<EVENTTYPE,
                           Transition<OPERAND, STATE, EVENTTYPE, EVENT>>>(prototype);

    for (TransitionsListNode cursor = transitionsListNode;
         cursor != null;
         cursor = cursor.next) {
      stack.push(cursor.transition);//入栈
    }

    while (!stack.isEmpty()) {
      stack.pop().apply(this);//构建拓扑表得具体实现 用了hashmap
    }
  }

  private interface Transition<OPERAND, STATE extends Enum<STATE>,
          EVENTTYPE extends Enum<EVENTTYPE>, EVENT> {
    STATE doTransition(OPERAND operand, STATE oldState,
                       EVENT event, EVENTTYPE eventType) throws InvalidStateTransitionException;
  }

  private class SingleInternalArc
                    implements Transition<OPERAND, STATE, EVENTTYPE, EVENT> {

    private STATE postState;
    private SingleArcTransition<OPERAND, EVENT,STATE> hook; // transition hook

    SingleInternalArc(STATE postState,
        SingleArcTransition<OPERAND,EVENT,STATE> hook) {
      this.postState = postState;
      this.hook = hook;
    }

    @Override
    public STATE doTransition(OPERAND operand, STATE oldState,
                              EVENT event, EVENTTYPE eventType) {
      if (hook != null) {
        hook.transition(operand, event,postState);
      }
      return postState;
    }
  }

  private class MultipleInternalArc
              implements Transition<OPERAND, STATE, EVENTTYPE, EVENT>{

    private Set<STATE> validPostStates;
    private MultipleArcTransition<OPERAND, EVENT, STATE> hook;  // transition hook

    MultipleInternalArc(Set<STATE> postStates,
                   MultipleArcTransition<OPERAND, EVENT, STATE> hook) {
      this.validPostStates = postStates;
      this.hook = hook;
    }

    @Override
    public STATE doTransition(OPERAND operand, STATE oldState,
                              EVENT event, EVENTTYPE eventType)
        throws InvalidStateTransitionException {
      STATE postState = hook.transition(operand, event);

      if (!validPostStates.contains(postState)) {
        throw new InvalidStateTransitionException(oldState, eventType);
      }
      return postState;
    }
  }

  /**
   * 返回一个状态机 如果未初始化拓扑表 则重新初始化
   * 1：传入操作者得具体实现类
   * 2：更改默认状态
   * @param operand
   * @param initialState
   * @return
   */
  public StateMachine<STATE, EVENTTYPE, EVENT>
        make(OPERAND operand, STATE initialState) {
    return new InternalStateMachine(operand, initialState);
  }

  public StateMachine<STATE, EVENTTYPE, EVENT> make(OPERAND operand) {
    return new InternalStateMachine(operand, defaultInitialState);
  }

  private class InternalStateMachine
        implements StateMachine<STATE, EVENTTYPE, EVENT> {
    private final OPERAND operand;
    private STATE currentState;

    InternalStateMachine(OPERAND operand, STATE initialState) {
      this.operand = operand;
      this.currentState = initialState;
      if (!optimized) {
        maybeMakeStateMachineTable();
      }
    }

    @Override
    public synchronized STATE getCurrentState() {
      return currentState;
    }

    /**
     * 状态转移
     * @param eventType
     * @param event
     * @return
     * @throws InvalidStateTransitionException
     */
    @Override
    public synchronized STATE doTransition(EVENTTYPE eventType, EVENT event)
         throws InvalidStateTransitionException  {
      currentState = StateMachineFactory.this.doTransition
          (operand, currentState, eventType, event);
      return currentState;
    }
  }
}
