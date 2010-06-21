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
package org.activiti.test.jobexecutor;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.activiti.ActivitiException;
import org.activiti.impl.ProcessEngineImpl;
import org.activiti.impl.interceptor.Command;
import org.activiti.impl.interceptor.CommandContext;
import org.activiti.impl.interceptor.CommandExecutor;
import org.activiti.impl.job.MessageImpl;
import org.activiti.impl.job.TimerImpl;
import org.activiti.impl.jobexecutor.JobExecutor;
import org.activiti.test.ActivitiTestCase;


/**
 * @author Tom Baeyens
 */
public class JobExecutorTestCase extends ActivitiTestCase {

  protected TweetHandler tweetHandler = new TweetHandler();
  
  protected void setUp() throws Exception {
    super.setUp();
    
    ProcessEngineImpl processEngineImpl = (ProcessEngineImpl)processEngine;
    processEngineImpl
      .getProcessEngineConfiguration()
      .getJobCommands()
      .addJobHandler(tweetHandler);
  }

  protected void tearDown() throws Exception {
    ProcessEngineImpl processEngineImpl = (ProcessEngineImpl)processEngine;
    processEngineImpl
      .getProcessEngineConfiguration()
      .getJobCommands()
      .removeJobHandler(tweetHandler);

    super.tearDown();
  }

  protected MessageImpl createTweetMessage(String msg) {
    MessageImpl message = new MessageImpl();
    message.setJobHandlerType("tweet");
    message.setJobHandlerConfiguration(msg);
    return message;
  }

  protected TimerImpl createTweetTimer(String msg, Date duedate) {
    TimerImpl timer = new TimerImpl();
    timer.setJobHandlerType("tweet");
    timer.setJobHandlerConfiguration(msg);
    timer.setDuedate(duedate);
    return timer;
  }

}