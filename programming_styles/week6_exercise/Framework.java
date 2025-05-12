// class main {
//     Class cls = null;
//     URL classUrl = null;
//     try {
//       // TODO: Grab URLS from a cfg file.
//       // WRONG!!!!
//       classUrl = new URL("file:///home/runner/ReflectionAndPlugins/deploy/app1.jar");
//     } catch (Exception e) {
//       e.printStackTrace();
//     }
//     URL[] classUrls = {classUrl};
//     URLClassLoader cloader = new URLClassLoader(classUrls);
//     try {
//       cls = cloader.loadClass();
//     } catch (Exception e) {
//       e.printStackTrace();
//     } 

//     if (cls != null) {
// 	    try {
//     		ISurprise adder = (ISurprise)cls.newInstance();
// 		    System.out.println("Surprise result: " + adder.surpriseOperation(4, 6));
// 	    } catch (Exception e) {
// 		    e.printStackTrace();
// 	    }

//     }
// }
