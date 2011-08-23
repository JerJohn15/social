/*
 * Copyright (C) 2003-2011 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.exoplatform.social.extras.benches;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.exoplatform.container.xml.InitParams;
import org.exoplatform.container.xml.ValueParam;
import org.exoplatform.services.bench.DataInjector;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          thanhvucong.78@gmail.com
 * Aug 4, 2011  
 */
public class ExoSocialDataInjector extends DataInjector {

  private static Log LOG = ExoLogger.getLogger(ExoSocialDataInjector.class);
  private long       numberOfUser;
  private long       numberOfRelation;
  private long       numberOfActivity;
  private long       numberOfSpace;
  private ExoSocialDataInjectionExecutor injector;
  private boolean isInitialized = false;
  
  private Map<String, Long> userActivities = new HashMap<String,Long>();
  
  public ExoSocialDataInjector(ExoSocialDataInjectionExecutor injector) {
    this.injector = injector;
  }
  
  @Override
  public Log getLog() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean isInitialized() {
    return isInitialized;
  }

  
  
  @Override
  public void initParams(InitParams initParams) {
    if (initParams != null) {
      //Gets the maximum the User using for creating Users
      ValueParam vParam = (ValueParam) initParams.get("mU");
      numberOfUser = longValue("mU", vParam.getValue());
      
      //Gets the maximum the Relationship using for creating Relationships
      vParam = (ValueParam) initParams.get("mR");
      numberOfRelation = longValue("mR", vParam.getValue());
      
      //Gets the maximum the Activity using for creating Activities
      vParam = (ValueParam) initParams.get("mA");
      numberOfActivity = longValue("mA", vParam.getValue());
    }
    
  }

  @Override
  public void inject() throws Exception {
    LOG.info("starting...");
    boolean nothingWasDone = true;
    if (numberOfUser > 0) {
      nothingWasDone = false;
      LOG.info("\t> about to inject " + numberOfUser + " people.");
      injector.generatePeople(numberOfUser);
    }
    if (numberOfRelation > 0) {
      nothingWasDone = false;
      LOG.info("\t> about to inject " + numberOfRelation + " connections.");
      injector.generateRelations(numberOfRelation);
    }
    if (numberOfActivity > 0) {
      nothingWasDone = false;
      LOG.info("\t> about to inject " + numberOfActivity + " activities.");
      injector.generateActivities(numberOfActivity);
    }

    if(! userActivities.isEmpty()) {
      Set<Entry<String,Long>> entries = userActivities.entrySet();
      for (Entry<String, Long> entry : entries) {
        String username = entry.getKey();
        Long count = entry.getValue();
        LOG.info("\t> about to inject " + count + " activities for " + username + ".");
        injector.generateActivities(username, count);
      }

    }


    if (nothingWasDone) {
      LOG.info("nothing to inject.");
    }
    isInitialized = true;
    
  }

  @Override
  public void reject() throws Exception {
    throw new UnsupportedOperationException();
  }
  
  /**
   * Gets Long Value from param value.
   * @param property
   * @param value
   * @return
   */
  private long longValue(String property, String value) {
    try {
      if (value != null) {
        return Long.valueOf(value);
      }
    } catch (NumberFormatException e) {
      LOG.warn("Long number expected for property " + property);
    }
    return 0;
  }

}