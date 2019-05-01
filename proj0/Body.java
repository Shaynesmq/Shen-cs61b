public class Body {
	static final double g = 6.67e-11;

	/** position, direction, mass, image depicts the body */
	public double xxPos;
	public double yyPos;
	public double xxVel;
	public double yyVel;
	public double mass;
	public String imgFileName;

	public Body(double xP, double yP, double xV,
				double yV, double m, String img) {
		xxPos = xP;
		yyPos = yP;
		xxVel = xV;
		yyVel = yV;
		mass = m;
		imgFileName = img;
	}

	public Body(Body b) {
		System.out.println("yes");
	}

	/** calculate the distance between this body and body b2 */
	public double calcDistance(Body b2) {
		double dx = b2.xxPos - this.xxPos;
		double dy = b2.yyPos - this.yyPos;
		double distance = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));

		return distance;
	}

	/** calculate the force exerted body by b2 on this body */
	public double calcForceExertedBy(Body b2) {
		double distance = this.calcDistance(b2);
		double force = (g * this.mass * b2.mass) / Math.pow(distance, 2);

		return force;
	}

	/** calculate the force exerted in the X direction */
	public double calcForceExertedByX(Body b2) {
		double dx = 0;
		double distance = 0;
		double force = 0;
		double forceX = 0;
		
		dx = b2.xxPos - this.xxPos;
		distance = calcDistance(b2);
		force = calcForceExertedBy(b2);
		forceX = (force * dx) / distance;

		return forceX;
	}

	/** calculate the force exerted in the Y direction */
	public double calcForceExertedByY(Body b2) {
		double dy = 0;
		double distance = 0;
		double force = 0;
		double forceY = 0;
		
		dy = b2.yyPos - this.yyPos;
		distance = calcDistance(b2);
		force = calcForceExertedBy(b2);
		forceY = (force * dy) / distance;

		return forceY;
	}

	/** calculate the net force in the X direction */
	public double calcNetForceExertedByX(Body[] allBodys) {
		double forceX = 0;
		double netForceX = 0;

		for (Body b : allBodys) {
			if (this.equals(b)) {
				continue;
			}
			forceX = this.calcForceExertedByX(b);
			netForceX = netForceX + forceX;
		}

		return netForceX;
	}

	/** calculate the net force in the Y direction */
	public double calcNetForceExertedByY(Body[] allBodys) {
		double forceY = 0;
		double netForceY = 0;

		for (Body b : allBodys) {
			if (this.equals(b)) {
				continue;
			}
			forceY = this.calcForceExertedByY(b);
			netForceY = netForceY + forceY;
		}

		return netForceY;
	}

	/** update the position based dt, fX, fY */
	public void update(double dt, double fX, double fY) {
		double aX = fX / this.mass;
		double aY = fY / this.mass;
		this.xxVel = this.xxVel + aX * dt;
		this.yyVel = this.yyVel + aY * dt;
		this.xxPos = this.xxPos + this.xxVel * dt;
		this.yyPos = this.yyPos + this.yyVel * dt;
	}

	/** draw one body using StdDraw */
	public void draw() {
		String filepath = "images/";
		filepath = filepath + this.imgFileName;

		StdDraw.picture(this.xxPos, this.yyPos, filepath);
	}
}