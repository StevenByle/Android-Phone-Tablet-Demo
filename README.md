# Optimizing for Android Phones and Tablets with Fragments

<p align="center">
  <img src="Screenshots/Phone%20Layout%20Screenshots%20Port.png" width=650/>
  <img src="Screenshots/Tablet%20Layout%20Screenshots.png" width=650/>
</p>

<h2>When Tablets Were Still Stone</h2><p>A long, long, time ago, when the Android framework was being developed in the mid-2000’s, modern mobile phone screens were still small, and mostly flip, slider, and candy bar form factors. Almost all input came from hardware buttons, as touch screens were just starting to make it into consumer products. With that in mind, the Android engineers designed a user interface framework that would be flexible enough to fit almost any screen size and aspect ratio, and adaptable enough to support a plethora of hardware configurations. However, the scope of Android at that time was focused around mobile phones, and large screen “tablet” devices were not even on the horizon yet. Actually, at that time, the term “tablet” was still used to reference what are now called “convertible laptops”.</p><h2>Activities and Views</h2><p>Android engineers ultimately came up with a relatively simple user interface framework. Layouts were based on Activities, which host View elements that the user can interact with. In an Model-View-Controller (MVC) architecture, an Activity acts as a Controller, while a View acts as (if it isn’t obvious) a View. User interface elements that user sees and interacts with are all Views: text, buttons, text fields, images, spinners, check boxes, switches, seek bars, lists, pagers, etc. Activities take those Views, arrange them into a screen layout, and manage their behavior as well as the application flow.</p><h2>Limitations of Activities</h2><p>While many things can be done with Activities, they have some limitations. Only a single Activity can be in a “resumed” state at a time, meaning the user can only interact with one Activity at a time. Activities also require control of the full screen when resumed, so Activities can’t share the screen with each other. However, Activities can be floating “dialog” windows and use transparency, but they still take focus of the entire screen, causing any visible Activities to be inactive.</p><p>So what’s the reason for these limitations? Well remember, early Android devices had very little RAM, as well as smaller screens. At the time, there were more performance detriments than gains if more than one Activity was allowed to be active at once. Also, there was little purpose of having multiple Activities on the screen at once. The same layout could suffice for every screen size, since those sizes didn’t vary much.<!--break-->

</p><h2>Why Fragments?</h2><p>The Activity and View architecture worked well for early Android devices, and were flexible enough to handle the varying phone shapes, sizes, orientations, and hardware configurations. However, with improvements in mobile processors, memory, and graphics, large screen devices dubbed “tablets” began hitting the market with 7” to 10” diagonal screens, at least four times the physical screen size as the average phone. In theory, all of this screen real estate would allow apps to venture beyond a single screen design, and incorporate much deeper user interfaces similar to desktop applications.</p>

<p>At this time, Android 2.3, known as Gingerbread, was still using the Activity and View architecture. Any application written to take advantage of new large tablet screens would still needed to work on phones, which were obviously still the primary market. With only the use of Activities, this task proved to be very difficult, since the user interface framework was never designed for large screen devices.</p>

<p>Most existing applications were designed with user interfaces built with single screen Activities that performed certain tasks. For instance, an email app may have a list Activity showing email titles and some content text on one screen. Upon selection of an email in the list, a detail Activity would shows the email in full on another screen. Now, with large tablet screens, there was room to show both the list and the detail screen at the same time. But, since only one Activity can be active at once, it wasn’t as easy as dropping the two Activities on the same screen. Due to the limitations of Activities, the only option was to use a single Activity to handle the list and the detail Views for tablet screens, and keep the single screen Activities for phone screens. However, this meant that the logic that controlled the behavior of the list and detail Activities had to be either copied into the new dual pane Activity, or the controller logic had to be shared using sophisticated class structures and interfaces. Neither solutions was optimal, which led to complex code bases that were difficult to maintain.</p>

<h3>The Shortcomings of Activities Alone</h3>
<ul>
	<li>Require full screen control when active.</li>
	<li>Cannot have more than one Activity active at once.</li>
	<li>Cannot contain sub-Activities, only Views.</li>
	<li>Supporting larger screen sizes requires copying logic, or complicated shared logic.</li>
</ul>

<p align="center"><img src="http://i.imgur.com/EB4jqLm.png" title="" width="550"></p>

<p>In order to solve this problem, Android engineers went back to work to allow Android to better optimize for tablets. With the release of Android 3.0, known as Honeycomb, Android introduced Fragments, a new component to create reusable user interfaces. Fragments are essentially sub-Activities, which can create their own layout of Views and behavior to manage. The key feature of is that multiple Fragments can be active at once, and they can use all, some, or none of the screen. An Activity is still required as the root of the user interface, so every Fragment must have a parent. Fragments can also have nested child Fragments. Like Activities, Fragments can also be stacked and later removed with the back button.</p><p>With the Fragment architecture, apps can simply encapsulate their logic into smaller pieces of functionality, and then reuse those pieces to best fit the device’s screen configuration. For instance, an email application can use a list Fragment to show a list of emails, and a detail Fragment to show the content of a selected email. On a phone, these Fragments can be used in a single Activity. Since there is less space on a phone, the list Fragments can take the full screen and the detail Fragment stack on top of the List Fragment when the user selects an email from the list. On a tablet, the list Fragment and detail Fragment have room to be displayed on the screen at the same time, and the detail Fragment can simply update when the user selects an email.</p>

<h3>The Benefits of Fragments</h3>

<ul>
	<li>Can use a portion of the display or no display at all.</li>
	<li>Allow logic to be encapsulated into smaller building blocks of the user interface, so it doesn’t need to be copied.</li>
	<li>Can be reused and added dynamically to support larger screen sizes and different configurations.</li>
</ul>

<p align="center"><img src="http://i.imgur.com/LGe7YZa.png" title="" width="550"></p>

<h2>Design Strategy</h2><p>While Fragments give a ton of flexibility to the user interface, they also require some code organization to be reusable and adaptable for diverse device configurations. In order for Fragments to perform well on phones, <a href="http://en.wikipedia.org/wiki/Phablet">phablets</a>, and tablets in all orientations, these guidelines should be followed:</p>
<ol>
	<li><strong>Break down the user interface into standalone Fragments, which perform a specific task.</strong>
		<ul>
			<li>A Fragment should be a piece of functionality that is modular, self-contained, with its own layout (if needed) and behavior.</li>
		</ul>
	</li>
	<li><strong>Create generic, flexible Fragments.</strong>
		<ul>
			<li>Ensure Fragments do not make assumptions about how they will be used, or what other components will be used with them, to prevent coupling of code.</li>
			<li>Allow Fragments to be configurable, allowing the look and behavior to be set at runtime.</li>
			<li>Make Fragment layouts stretchable, optimize for small or large amounts of space.</li>
		</ul>
	</li>
	<li><strong>Use resource identifiers to optimize for different displays.</strong>
		<ul>
			<li>Provide alternate resources (images, font sizes, layouts, etc.) for different screen sizes, orientations, and <a href="https://github.com/StevenByle/Android-Density-Independence-Demo">pixel densities</a>.</li>
		</ul>
	</li>
	<li><strong>Require the parent Activity (or parent Fragment) to manage all of its child Fragments’ communication.</strong>
		<ul>
			<li>Fragments should only be aware of their parent and communicate with it through interfaces.</li>
			<li>Parent Activities (or parent Fragments) should handle communication from their children Fragments, and take necessary action based upon the current application state.</li>
			<li>Fragments should not interact with sibling Fragments directly, since there is no guarantee of the existence or state of sibling Fragments.</li>
		</ul>
	</li>
	
</ol>

<h2>Creating Fragments</h2><p>Fragments can be created two different ways: statically in the layout XML or dynamically in code. Creating Fragments statically is simpler, yet more restricting since static Fragments cannot be replaced or removed at runtime. Dynamically adding Fragments at runtime is slightly more complicated, but more flexible by allowing Fragments to be replaced, removed, and stacked on top of each other at runtime, just like Activities.</p>

<ol>
	<li><strong>Embedding Fragments into XML layout files.</strong>
		<ul>
			<li>Fragments are added in the layout file, just like a <code>View</code>, and inflated by the parent, and added to the <code>FragmentManager</code>.</li>
			<li>These Fragments cannot be removed at runtime, meaning they cannot be replaced or stacked either.</li>
		</ul>
	</li>
	<li><strong>Adding Fragments at runtime, in code.</strong>
		<ul>
			<li>Parent Activities (or parent Fragments) must have layouts with containers for their children Fragments. The containers must be <code>ViewGroups</code>, and are usually <code>FrameLayouts</code>.
			</li>
			<li>The parent should inspect the layout it is provided (depending on resource identifiers), and add the appropriate Fragments according to which containers are in the layout.</li>
			<li>This approach is more flexible, and allows the parent to add, replace, and remove Fragments as needed at runtime.</li>
		</ul>
	</li>
</ol>

<h2>Handling Fragment Communication</h2><p>While Fragments are standalone pieces of functionality that can operate on their own, they also should be modular, and able to work with other components of the user interface. Fragments must be able to communicate with other components of the application to relay events and information. As discussed in a earlier, an email app with a list Fragment must be able to inform the detail Fragment which email was selected so the detail Fragment can display the right email. Likewise, if the user swipes to the next email in the detail Fragment, it must inform the list Fragment to update which email is highlighted. To avoid coupling, this Fragment communication needs to be structured, and organized.</p>

<ol>
	<li><strong>Allow Fragments to only communicate with their parent.</strong>
		<ul>
			<li>The parent Activity (or parent Fragment) has the big picture of what Fragments will be used in its layouts, where as the children Fragments do not.</li>
			<li>Fragments can only assume the existence of their parent, and can never assume existence or state of sibling Fragments.</li>
			<li>Child Fragments should communicate directly to their parent. The parent will then know what other children Fragments or other components to notify, if any.</li>
		</ul>
	</li>
	<li><strong>Use interfaces to communicate between Fragment and parent.</strong>
		<ul>
			<li>Have the parent Activity (or parent Fragment) implement any interfaces that its children will use to notify it of events.</li>
			<li>Likewise, have the child Fragment implement any interfaces that its parent will use to notify it of events.</li>
		</ul>
	</li>
	<li><strong>As an alternative to parental driven communication, use global event buses to relay communication</strong>
		<ul>
			<li>The event bus should deliver messages to registered and active listeners.</li>
			<li>This prevents direct Fragment communication.</li>
		</ul>
	</li>
</ol>

<h2>Layout Dependent Actions</h2><p>Since Fragments are flexible, they provide the user more or less functionality depending on the device and its configuration. So when the user interface needs to take an action, it may require different steps to complete that action depending on the state of its Fragments. Controller code must be aware of the multiple states of the user interface, and take the appropriate action depending on that state.</p><ol><li>If a Fragment needed is not currently in the layout, create it and add it to the layout, or stack it on top if there is not enough room.</li><li>If a Fragment needed is optional (tablet only), and is not currently in the layout, ignore the update.</li><li>If a Fragment needed is currently in the layout, simply update it using an interface method.</li></ol>

<h2>Example Application</h2><p>While guidelines and code snippets are nice, only a working example application can really demonstrate these concepts.&nbsp;Consider an example application with some arbitrary requirements designed to highlight the topics covered so far. For instance, say the app should allow the user to view a set of images, and be able select those images to manipulate. The app should also optimize for any size display, in any orientation.</p>

<ol>
	<li><strong>The application user interface must be flexible.</strong>
		<ol>
			<li>It must optimize for portrait and landscape.
				<ul>
					<li>All visual state must be kept between orientation changes.</li>
				</ul>
			</li>
			<li>It must optimize for large and small screens.</li>
		</ol>
	</li>
	<li><strong>The user must be able to browse and select a set of images.</strong>
		<ol>
			<li>The images and titles must display in a scrollable list.</li>
			<li>The images and titles must display in a horizontal swiping pager.</li>
			<li>Images must be able to be selected and manipulated.
				<ul>
					<li>If there is room to manipulate the image on the same screen, selecting from the list or pager should select that image to be manipulated.</li>
					<li>If there is not room to manipulate the image on the same screen, a menu button must allow the user to select the currently focused image. Upon pressing the menu button, the app must navigate to a new screen where the image can be manipulated.</li>
				</ul>
			</li>
			<li>Selecting an image on the list should focus the same image in the pager, and vice versa.</li>
		</ol>
	</li>
	<li><strong>The user must be able to manipulate a selected image.</strong>
		<ol>
			<li>The image must be able to scale from 0 to 5 times its original size.
				<ul>
					<li>In the X, and Y dimensions.</li>
				</ul>
			</li>
			<li>The image must be able to translate between the provided bounds of space.
				<ul>
					<li>In the X, and Y dimensions.</li>
					<li>Image positions should be represented and stored as percentages of the total available space.</li>
				</ul>
			</li>
			<li>The image must be able to rotate from 0 to 360 degrees.
				<ul>
					<li>On the X, Y, and Z axes.</li>
				</ul>
			</li>
		</ol>
	</li>
</ol>


<h2>Application Architecture</h2><p>From the requirements, it’s obvious there should be at least two Fragments: one for viewing and selecting the images and one for manipulating them. However, showing the images in a list and in a pager are mutually exclusive tasks, and can operate independent of each other. This means the image selector Fragment can be split into two children Fragments, an image list Fragment and an image pager Fragment. So, there are three bottom-level Fragments that will handle the direct functions of the app, and one parent Fragment with one parent Activity to manage communication and layouts. This architecture is a simple, yet structured approach to using Fragments.</p>

<ol>
	<li><strong>Main Activity</strong>
		<ol>
			<li>Image Selector Fragment
				<ol>
					<li>Image List Fragment</li>
					<li>Image Pager Fragment</li>
				</ol>
			</li>
			<li>Image Rotator Fragment</li>
		</ol>
	</li>
</ol>

<h2>Layouts For Smaller Devices</h2><p>Since phone displays have limited amounts of space, it will take an entire screen to let the user select an image, and another screen to let the user manipulate that selected image. In portrait, stacking the list and pager Fragments vertically is a good use of space, while putting them side by side is the better approach for landscape.</p>


<p align="center"><img src="http://i.imgur.com/B77pma2.png" title="" width="550"></p>

<p>For image manipulation, the rotator Fragment will simply take up the entire screen in both portrait and landscape, since more space will be required for the rotator controls and showing the image.</p>

<p align="center"><img src="http://i.imgur.com/1P3pkfK.png" title="" width="550"></p>

<h2>Layouts For Larger Devices</h2><p>On devices with at least 7” screens, the list, pager, and rotator Fragments should all be able to fit on the screen at the same time. More screen space should be dedicated to the rotator Fragment, since it must have room for user controls and displaying the image. In portrait, putting the list and pager Fragments side by side on top, with the rotator on bottom gives a good amount of room for each Fragment. In landscape, stacking the list and pager Fragments vertically on one side, with the rotator Fragment on the other side also gets the most out of the available space.</p>

<p align="center"><img src="http://i.imgur.com/cmuaCBA.png" title="" width="550"></p>

<h2>The Final Product</h2>

<p>Using the code architecture above, the <code>MainActivity</code> and <code>ImageSelectorFragment</code> will be used to marshal Fragment communication and handle the application navigation. The <code>ImageListFragment</code>, <code>ImagePagerFragment</code>, and <code>ImageRotatorFragment</code> will each implement their specific functionality, and use interfaces to inform their parents of events. In order to handle the 3D axis rotations, methods from API 11 (Android 3.0 / Honeycomb) are required. So, this example application will only run on Android 3.0 and higher devices (or emulators). However, the use of Fragments on pre-Honeycomb devices is made possible with the <a href="http://developer.android.com/tools/extras/support-library.html">Android Support Library</a>, which supports back to API 4 (Android 1.6 / Donut).</p>

<p align="center"><img src="http://i.imgur.com/muXHeFL.png" title="" width="600"></p>

<p align="center"><img src="http://i.imgur.com/GYRU9YT.png" title="" width="600"></p>

<p align="center"><img src="http://i.imgur.com/qnJooQV.png" title="" width="600"></p>

<h2>Summary</h2><p>Fragments are powerful tools used for building dynamic, flexible, and optimized user interfaces for Android applications. However, an organized design and architecture is required to get the best use of Fragments.</p>

<ol>
	<li>Break application user interfaces down into basic standalone components, and use Fragments to implement those components.</li>
	<li>Create flexible and adaptable Fragments, that don’t make assumptions.</li>
	<li>Handle Fragment communication through Fragment parents or global event buses.</li>
	<li>Use Fragments even when alternate layouts are not used by the application, which will make adding tablet support much easier later on.</li>
</ol>

<p>Android has come a long way in a short period of time, and will continue to evolve with mobile devices. By following some simple guidelines, it is possible to optimize applications to take advantage of every Android device.</p>

## License

    Copyright 2013 Steven Byle
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
    http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
