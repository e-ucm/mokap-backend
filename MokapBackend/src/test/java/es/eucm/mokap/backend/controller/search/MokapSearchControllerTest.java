/**
 *  Copyright [2014] [mokap.es]
 *
 *    This file is part of the mokap community backend (MCB).
 *    MCB is licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package es.eucm.mokap.backend.controller.search;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.*;

public class MokapSearchControllerTest {

	public static final String BASE_URL = "http://test.mokap.es/thumbnails/";

	@Test
	public void testThumbnailPickup() {

		String[] widths = new String[] { "512", "256", "128", "64", "32" };
		String[] heights = new String[] { "512", "256", "128", "64", "32" };

		doTestThumbnailPickup(widths, heights, "256", "256", BASE_URL
				+ "256x256.png");
		doTestThumbnailPickup(widths, heights, "1024", "1024", BASE_URL
				+ "512x512.png");
		doTestThumbnailPickup(widths, heights, "129", "129", BASE_URL
				+ "256x256.png");
		doTestThumbnailPickup(widths, heights, "127", "127", BASE_URL
				+ "128x128.png");

		heights = new String[] { "16", "32", "64", "128", "256" };
		widths = new String[] { "32", "64", "128", "256", "512" };
		doTestThumbnailPickup(widths, heights, "512", "512", BASE_URL
				+ "512x256.png");
		doTestThumbnailPickup(widths, heights, "256", "256", BASE_URL
				+ "512x256.png");
	}

	public void doTestThumbnailPickup(String[] widths, String[] heights,
			String preferredWidth, String preferredHeight, String bestChoice) {
		List<String> thumbnails = new ArrayList<String>();
		for (int i = 0; i < widths.length; i++) {
			thumbnails.add(BASE_URL + widths[i] + "x" + heights[i] + ".png");
		}

		MokapSearchController controller = new MokapSearchController();
		assertEquals("Not the expected thumbnail!", bestChoice,
				controller.pickThumbnail(thumbnails, preferredWidth,
						preferredHeight));
	}

}
