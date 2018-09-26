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
private EasyModel em;
	public Model(Vector position, Vector speed, BigDecimal mass) {
		Position=position;
		Speed=speed.clone();
		Speed.Multiply(new BigDecimal(1000));
		Mass=mass;
		h=new BigDecimal(1);
		Accu=50;
		MethodID=2;
		os = new BigDecimal(1).divide(new BigDecimal(6), 30, BigDecimal.ROUND_DOWN);
		em=new EasyModel(position, speed, mass);
	}
	
	// Velocity in m/s
	// Acceleration in m/s^2
	// Distance in km*1000
	
	public void SetStep(BigDecimal a)
	{
		h=a;
		em.SetStep(a);
	}
	
	public void SetAccuracy(int a)
	{
		Accu=a;
		os = new BigDecimal(1).divide(new BigDecimal(6), a, BigDecimal.ROUND_DOWN);
		em.SetAccuracy(a);
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
			v=em.PerformStep();
		Position = new Vector(Position.X.setScale(Accu, BigDecimal.ROUND_DOWN), Position.Y.setScale(Accu, BigDecimal.ROUND_DOWN));
		Speed = new Vector(Speed.X.setScale(Accu, BigDecimal.ROUND_DOWN), Speed.Y.setScale(Accu, BigDecimal.ROUND_DOWN));
		return v;
	}
	
	private Vector Euler()
	{
		Vector acc = GetAccel(Position.clone());
		Vector sp = acc.clone();
		sp.Multiply(h.multiply(new BigDecimal("0.5")));
		sp.Apply(Speed.clone());
		Position = GetPosition(Position.clone(), sp.clone(), BigDecimal.ONE);
		Speed = GetVelocity(Speed.clone(), acc, BigDecimal.ONE);
		return Position;
	}
	
	private Vector EulerCauchy()
	{
		Vector acc0 = GetAccel(Position.clone());
		Vector sp0 = acc0.clone();
		sp0.Multiply(h.multiply(new BigDecimal("0.5")));
		sp0.Apply(Speed.clone());
		Vector x1 = GetPosition(Position.clone(), sp0.clone(), BigDecimal.ONE);
		Vector acc1 = GetAccel(x1.clone());
		Vector acc = acc0.clone();
		acc.Apply(acc1.clone());
		acc.Multiply(new BigDecimal("0.5"));
		Vector sp = acc.clone();
		sp.Multiply(h.multiply(new BigDecimal("0.5")));
		sp.Apply(Speed.clone());
		Position = GetPosition(Position.clone(), sp.clone(), BigDecimal.ONE);
		Speed = GetVelocity(Speed.clone(), acc, BigDecimal.ONE);
		return Position;
	}

	private Vector GetAccel(Vector pos)
	{
		BigDecimal r = pos.X.multiply(pos.X);
		r=r.add(pos.Y.multiply(pos.Y));
		r=sqrt(r, Accu);
		BigDecimal a = Mass.multiply(new BigDecimal("0.667408313131313131313131313131313131313131313131313131").setScale(Accu, RoundingMode.FLOOR));
		a=a.divide(r, BigDecimal.ROUND_DOWN);
		a=a.divide(r, BigDecimal.ROUND_DOWN);
		Vector va = new Vector(pos.X, pos.Y);
		va.Multiply(a.divide(r, BigDecimal.ROUND_DOWN));
		va.Multiply(new BigDecimal(-1));
		return va;//m/s^2
	}
	
	private Vector GetPosition(Vector pos, Vector st_vel, BigDecimal part)
	{
		Vector midvel = st_vel.clone();
		midvel.Multiply(h.multiply(new BigDecimal("0.000001")));
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
