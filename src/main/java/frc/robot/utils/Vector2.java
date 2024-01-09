package frc.robot.utils;

// import org.w3c.dom.views.DocumentView;

public class Vector2 {
    
    public double x;
    public double y; 
    
    public Vector2() {
        x = 0;
        y = 0;
    }

    public Vector2(double x, double y){
        this.x = x;
        this.y = y;
    }

    public static Vector2 fromPolar(double theta, double r){
        return new Vector2(Math.cos(theta) * r, Math.sin(theta) * r);
    }

    /**
    * Add another vector to this one
    * @param rhs The vector on the right hand side of the equation
    */
    public Vector2 add(Vector2 rhs){
        return new Vector2(x + rhs.x, y + rhs.y);
    }
    /**
    * Subtract another vector from this one
    * @param rhs The vector on the right hand side of the equation
    */
    public Vector2 sub(Vector2 rhs){
        return new Vector2(x - rhs.x, y - rhs.y);
    }
    /**
    * Multiply this vector by a scalar
    * @param scale The scalar to multiply the vector by
    */
    public Vector2 mul(double scale){
        return new Vector2(x * scale, y * scale);
    }
    /**
    * Divide this vector by a scalar
    * @param scale The scalar to divide the vector by
    */
    public Vector2 div(double scale){
        return new Vector2(x / scale, y / scale);
    }
    /**
    * Calculate the magnitude of this vector
    */
    public double mag(){
        return Math.hypot(x, y);
    }
    /**
    * Calculate a normalized version of this vector
    */
    public Vector2 norm(){
        // x/=mag();
        // y/= mag();
        double m = mag();
        return div(m);
    }

    /**
    * Calculate a rotated version of this vector using a rotation matrix
    @param angle The angle, in radians, by which to rotate the vector. Positive = counter clockwise
    */
    public Vector2 rotate(double angle){
        double rotateX = x * Math.cos(angle) - y * Math.sin(angle);
        double rotateY = x * Math.sin(angle) + y * Math.cos(angle);
        return new Vector2(rotateX, rotateY);
    }

    public double dot(Vector2 b){
        return x*b.x + y*b.y;
    }

    /**
    * Calculate the angle of the vector's direction using Math.atan2
    * Circle begins at (1,0), positive rotating counter clockwise 
    @return Angle in radians 
    */
    public double atan2(){
        return Math.atan2(y, x);
    }
    
    @Override
    public String toString(){
        return String.format("(%.2f, %.2f)", x, y);
    }

}
