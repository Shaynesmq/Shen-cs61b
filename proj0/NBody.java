public class NBody {
	/** read the radius */
	public static double readRadius(String filename) {
		In planets = new In(filename);
		int number = planets.readInt();
		double radius = planets.readDouble();
		return radius;
	}

	/** read information of planets and return a Body array */
	public static Body[] readBodies(String filename) {
		In planets = new In(filename);
		int number = planets.readInt();
		double radius = planets.readDouble();

		Body[] allBodies = new Body[number];
		for (int i = 0; i < number; i++) {
			double xxPos = planets.readDouble();
			double yyPos = planets.readDouble();
			double xxVel = planets.readDouble();
			double yyVel = planets.readDouble();
			double mass = planets.readDouble();
			String imgFileName = planets.readString();
			allBodies[i] = new Body(xxPos, yyPos, xxVel, yyVel, mass, imgFileName);
		}

		return allBodies;
	}

	public static void main(String[] args) {
		double T = Double.parseDouble(args[0]);
		double dt = Double.parseDouble(args[1]);
		String filename = args[2];

		int waitTimeMilliseconds = 10;
		double radius = readRadius(filename);
		Body[] allBodies = readBodies(filename);

		StdDraw.enableDoubleBuffering();
		StdDraw.setScale(-radius, radius);

		for (double time = 0; time < T; time = time + dt) {
			double[] xForces = new double[allBodies.length];
			double[] yForces = new double[allBodies.length];
			
			for (int i = 0; i < allBodies.length; i++) {
				xForces[i] = allBodies[i].calcNetForceExertedByX(allBodies);
				yForces[i] = allBodies[i].calcNetForceExertedByY(allBodies);
			}

			for (int i = 0; i < allBodies.length; i++) {
				allBodies[i].update(dt, xForces[i], yForces[i]);
			}

			StdDraw.picture(0, 0, "images/starfield.jpg");
			for (int i = 0; i < allBodies.length; i++) {
				allBodies[i].draw();
			}

			StdDraw.show();
			StdDraw.pause(waitTimeMilliseconds);
		}

		StdOut.printf("%d\n", allBodies.length);
		StdOut.printf("%.2e\n", radius);
		for (int i = 0; i < allBodies.length; i++) {
    		StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                  		  allBodies[i].xxPos, allBodies[i].yyPos, allBodies[i].xxVel,
                  		  allBodies[i].yyVel, allBodies[i].mass, allBodies[i].imgFileName);   
		}
	}
}