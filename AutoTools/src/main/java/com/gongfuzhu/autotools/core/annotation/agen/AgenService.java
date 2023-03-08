/*
 * Copyright 2019 EPAM Systems
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
package com.gongfuzhu.autotools.core.annotation.agen;

import com.epam.reportportal.listeners.ItemStatus;
import io.reactivex.Maybe;

/**
 * Describes all operations for com.epam.reportportal.testng RP listener handler
 */

public interface AgenService {

	/**
	 * Start current launch
	 */
	void startLaunch();

	/**
	 * Finish current launch
	 */
	void finishLaunch(ItemStatus status);

	/**
	 * Start test suite event handler
	 *
	 */
	void startTestSuite(String suiteName);

	/**
	 * Finish test suite event handler
	 *
	 */
	void finishTestSuite(Maybe<String> itemId,String suiteStatus);

	/**
	 * Start test event handler
	 *
	 */
	void startTest(String moduleName);

	/**
	 * Finish test event handler
	 *
	 */
	void finishTest();

	/**
	 * Start test method event handler
	 *
	 */
	void startTestMethod();

	/**
	 * Finish test method event handler
	 *
	 * @param status     Status (PASSED/FAILED)
	 * @see ItemStatus
	 * @deprecated
	 */
	@Deprecated
	void finishTestMethod(String status);

	/**
	 * Finish test method event handler
	 *
	 * @param status     Status (PASSED/FAILED)
	 * @see ItemStatus
	 */
	void finishTestMethod(ItemStatus status);

	/**
	 * Start configuration method(any before of after method)
	 *
	 * @param testResult TestNG's test result
	 */
//	void startConfiguration(ITestResult testResult);
//
//	void sendReportPortalMsg(ITestResult testResult);
}
