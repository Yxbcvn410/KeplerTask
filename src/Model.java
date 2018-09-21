import java.math.BigDecimal;
import java.math.RoundingMode;

public class Model {
private Vector Position;
private Vector Speed;
private BigDecimal Mass;
private BigDecimal h;
private BigDecimal os;
private int Accu;
private int MethodID;
	public Model(Vector position, Vector speed, BigDecimal mass) {
		Position=position;
		Speed=speed;
		Speed.Multiply(new BigDecimal(1000));
		Mass=mass;
		h=new BigDecimal(1);
		Accu=50;
		MethodID=2;
		os = new BigDecimal(1).divide(new BigDecimal(6), 30, BigDecimal.ROUND_DOWN);
	}
	
	// Velocity in m/s
	// Acceleration in m/s^2
	// Distance in km*1000
	
	public void SetStep(BigDecimal a)
	{
		h=a;
	}
	
	public void SetAccuracy(int a)
	{
		Accu=a;
	}
	
	public void SetMethod(int id)
	{
		MethodID=id;
	}
	
	public Vector PerformStep()
	{
		Vector v = EulerCauchy();
		if(MethodID==0)
			v = Euler();
		else if(MethodID==1)
			v = EulerCauchy();
		else if(MethodID==2)
			v=RungeKutta2();
		else if(MethodID==3)
			v=RungeKutta4();
		Position = new Vector(Position.X.setScale(Accu, BigDecimal.ROUND_DOWN), Position.Y.setScale(Accu, BigDecimal.ROUND_DOWN));
		Speed = new Vector(Speed.X.setScale(Accu, BigDecimal.ROUND_DOWN), Speed.Y.setScale(Accu, BigDecimal.ROUND_DOWN));
		return v;
	}
	
	private Vector Euler()
	{
		Vector acc = GetAccel(Position.clone());
		Position = GetPosition(Position.clone(), Speed.clone(), acc, BigDecimal.ONE);
		Speed = GetVelocity(Speed.clone(), acc, BigDecimal.ONE);
		return Position;
	}
	
	private Vector EulerCauchy()
	{
		Vector acc0 = GetAccel(Position.clone());
		Vector x1 = GetPosition(Position.clone(), Speed.clone(), acc0, BigDecimal.ONE);
		Vector acc1 = GetAccel(x1.clone());
		Vector acc = acc0.clone();
		acc.Apply(acc1.clone());
		acc.Multiply(new BigDecimal(0.5));
		Position = GetPosition(Position.clone(), Speed.clone(), acc, BigDecimal.ONE);
		Speed = GetVelocity(Speed.clone(), acc, BigDecimal.ONE);
		return Position;
	}
	
	private Vector RungeKutta2()
	{
		Vector acc0 = GetAccel(Position.clone());
		Vector x1 = GetPosition(Position.clone(), Speed.clone(), acc0, new BigDecimal(0.5));
		Vector acc = GetAccel(x1.clone());
		Position = GetPosition(Position.clone(), Speed.clone(), acc, BigDecimal.ONE);
		Speed = GetVelocity(Speed.clone(), acc, BigDecimal.ONE);
		return Position;
	}
	
	private Vector RungeKutta4()
	{
		Vector acc0 = GetAccel(Position.clone());
		Vector x1 = GetPosition(Position.clone(), Speed.clone(), acc0.clone(), new BigDecimal(0.5));
		Vector acc1 = GetAccel(x1.clone());
		Vector x2 = GetPosition(Position.clone(), Speed.clone(), acc1.clone(), new BigDecimal(0.5));
		Vector acc2 = GetAccel(x2.clone());
		Vector x3 = GetPosition(Position.clone(), Speed.clone(), acc2.clone(), BigDecimal.ONE);
		Vector acc3 = GetAccel(x3.clone());
		acc1.Multiply(new BigDecimal(2));
		acc2.Multiply(new BigDecimal(2));
		Vector acc = acc0.clone();
		acc.Apply(acc1);
		acc.Apply(acc2);
		acc.Apply(acc3);
		acc.Multiply(os);
		
		Position = GetPosition(Position.clone(), Speed.clone(), acc, BigDecimal.ONE);
		Speed = GetVelocity(Speed.clone(), acc, BigDecimal.ONE);
		return Position;
	}
	
	private Vector GetAccel(Vector pos)
	{
		BigDecimal r = pos.X.multiply(pos.X);
		r=r.add(pos.Y.multiply(pos.Y));
		r=sqrt(r, Accu);
		BigDecimal a = Mass.multiply(new BigDecimal(0.667408));
		a=a.divide(r, BigDecimal.ROUND_DOWN);
		a=a.divide(r, BigDecimal.ROUND_DOWN);
		Vector va = new Vector(pos.X, pos.Y);
		va.Multiply(a.divide(r, BigDecimal.ROUND_DOWN));
		va.Multiply(new BigDecimal(-1));
		return va;//m/s^2
	}
	
	private Vector GetPosition(Vector pos, Vector st_vel, Vector acc, BigDecimal part)
	{
		Vector midvel = acc.clone();
		midvel.Multiply(h.multiply(part.multiply(new BigDecimal(0.5))));
		midvel.Apply(st_vel);
		midvel.Multiply(h.multiply(new BigDecimal(0.000001)));
		pos.Apply(midvel);
		return pos;
	}
	
	private Vector GetVelocity(Vector st_vel, Vector acc, BigDecimal part)
	{
		Vector fvel = acc.clone();
		fvel.Multiply(h.multiply(part));
		fvel.Apply(st_vel);
		return fvel;
	}
	
	public static BigDecimal sqrt(BigDecimal in, int scale){
	    BigDecimal sqrt = new BigDecimal(1);
	    sqrt.setScale(scale + 3, RoundingMode.FLOOR);
	    BigDecimal store = new BigDecimal(in.toString());
	    boolean first = true;
	    do{
	        if (!first){
	            store = new BigDecimal(sqrt.toString());
	        }
	        else first = false;
	        store.setScale(scale + 3, RoundingMode.FLOOR);
	        sqrt = in.divide(store, scale + 3, RoundingMode.FLOOR).add(store).divide(
	                BigDecimal.valueOf(2), scale + 3, RoundingMode.FLOOR);
	    }while (!store.equals(sqrt));
	    return sqrt.setScale(scale, RoundingMode.FLOOR);
	}
}
