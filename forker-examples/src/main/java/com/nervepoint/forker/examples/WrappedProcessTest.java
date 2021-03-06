/**
 * Copyright © 2015 - 2018 SSHTOOLS Limited (support@sshtools.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nervepoint.forker.examples;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.SystemUtils;

import com.sshtools.forker.client.ForkerBuilder;
import com.sshtools.forker.wrapper.ForkerWrapper;
import com.sshtools.forker.wrapper.ForkerWrapper.KeyValuePair;
import com.sshtools.forker.wrapper.WrapperIO;
import com.sshtools.forker.wrapper.WrapperProcessFactory;

/**
 * Another way of embedded {@link ForkerWrapper}, this time by using the
 * {@link ForkerBuilder} with an I/O mode of {@link WrapperIO}.
 */
public class WrappedProcessTest {

	public static void main(String[] args) throws Exception {
		// Standard builder creation
		ForkerBuilder fb = new ForkerBuilder();

		// Choose a command to run based on OS
		if (SystemUtils.IS_OS_UNIX)
			fb.parse("ls /etc");
		else if (SystemUtils.IS_OS_WINDOWS)
			fb.parse("DIR C:\\");
		else
			throw new UnsupportedOperationException("Add a command for your OS to test.");

		// Get a handle on the process factory, allowing configuration of it
		WrapperProcessFactory processFactory = fb.configuration().processFactory(WrapperProcessFactory.class);

		processFactory.addOption(new KeyValuePair("native", "true"));
		processFactory.addOption(new KeyValuePair("level", "FINEST"));
		processFactory.addOption(new KeyValuePair("restart-on", "0"));
		processFactory.addOption(new KeyValuePair("restart-wait", "10"));

		// Launches a new JVM
		processFactory.setSeparateProcess(true);

		// Tell forker builder we want a wrapped process
		fb.io(WrapperIO.WRAPPER);

		// Boilerplate stuff
		fb.redirectErrorStream(true);
		Process p = fb.start();
		IOUtils.copy(p.getInputStream(), System.out);
		System.out.println(" (" + p.waitFor() + ")");

	}

}
