/*	
 
  	Free Clinic Project
 
 	Copyright (c) 2007, 2008  Free Clinic Project  
 		A project by students of the University of California San Diego.  All rights reserved.
	
	This file is part of the Free Clinic Project.

    The Free Clinic Project is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    The Free Clinic Project is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with the Free Clinic Project.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.freeclinic.android.service;

import org.freeclinic.android.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

//TODO: Add simple class description.
/**
 * @author Henry Koren
 *
 */
public class FCPServiceController extends Activity {
    @Override
	protected void onCreate(Bundle icicle)
    {
        super.onCreate(icicle);

        setContentView(R.layout.service_controller);

        // Watch for button clicks.
        Button button = (Button)findViewById(R.id.start);
        button.setOnClickListener(mStartListener);
        button = (Button)findViewById(R.id.stop);
        button.setOnClickListener(mStopListener);
    }

    private OnClickListener mStartListener = new OnClickListener()
    {
        public void onClick(View v)
        {
            // Make sure the service is started.  It will continue running
            // until someone calls stopService().
            startService(new Intent(FCPServiceController.this,
                    FCPService.class), null);
        }
    };

    private OnClickListener mStopListener = new OnClickListener()
    {
        public void onClick(View v)
        {
            // Cancel a previous call to startService().  Note that the
            // service will not actually stop at this point if there are
            // still bound clients.
           stopService(new Intent(FCPServiceController.this,
                    FCPService.class)); 
        }
    };

}
