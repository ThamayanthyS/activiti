/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.activiti.test.bpmn.event.timer;

import java.util.Date;
import java.util.List;

import org.activiti.Job;
import org.activiti.JobQuery;
import org.activiti.ProcessInstance;
import org.activiti.Task;
import org.activiti.impl.time.Clock;
import org.activiti.test.ActivitiTestCase;


/**
 * @author jbarrez
 */
public class BoundaryTimerEventTest extends ActivitiTestCase {
  
  /*
   * Test for when multiple boundary timer events are defined on the same user task
   * 
   * Configuration:
   *   - timer 1 -> 2 hours -> secondTask
   *   - timer 2 -> 1 hour  -> thirdTask
   *   - timer 3 -> 3 hours -> fourthTask
   */
  public void testMultipleTimersOnUserTask() {
    deployProcessForThisTestMethod();
    
    // Set the clock to time '0'
    Clock.setCurrentTime(new Date(0L));
    
    // After process start, there should be 3 timers created
    ProcessInstance pi = processService.startProcessInstanceByKey("multipleTimersOnUserTask");
    JobQuery jobQuery = managementService.createJobQuery().processInstanceId(pi.getId());
    List<Job> jobs = jobQuery.list();
    assertEquals(3, jobs.size());
    
    // After setting the clock to time '1 hour and 5 seconds', the second timer should fire
    Clock.setCurrentTime(new Date( (60 * 60 * 1000) + 5000));
    waitForJobExecutorToProcessAllJobs(5000L, 25L);
    assertEquals(0L, jobQuery.count());
    
    // which means that the third task is reached
    Task task = taskService.createTaskQuery().singleResult();
    assertEquals("Third Task", task.getName());
  }

}