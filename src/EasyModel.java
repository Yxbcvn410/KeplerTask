import java.math.*;

public class EasyModel {
	private BigDecimal PositionX;
	private BigDecimal PositionY;
	private BigDecimal SpeedX;
	private BigDecimal SpeedY;
	private BigDecimal Mass;
	private BigDecimal h;
	private BigDecimal os;
	private int Accu;

	public EasyModel(Vector position, Vector speed, BigDecimal mass) {
		speed.Multiply(new BigDecimal(1000));
		PositionX = position.X;
		PositionY = position.Y;
		SpeedX = speed.X;
		SpeedY = speed.Y;
		Mass = mass;
		h = new BigDecimal(1);
		Accu = 50;
		os = new BigDecimal(1).divide(new BigDecimal(6), 30, RoundingMode.FLOOR);
	}

	// Velocity in m/s
	// Acceleration in m/s^2
	// Distance in km*1000

	public void SetStep(BigDecimal a) {
		h = a;
	}

	public void SetAccuracy(int a) {
		Accu = a;
		os = new BigDecimal(1).divide(new BigDecimal(6), a, RoundingMode.FLOOR);
	}

	public Vector PerformStep() {
		BigDecimal PosXdif1 = GetPosXdif(PositionX, PositionY, SpeedX, SpeedY);
		BigDecimal PosYdif1 = GetPosYdif(PositionX, PositionY, SpeedX, SpeedY);
		BigDecimal VelXdif1 = GetVelXdif(PositionX, PositionY, SpeedX, SpeedY);
		BigDecimal VelYdif1 = GetVelYdif(PositionX, PositionY, SpeedX, SpeedY);

		BigDecimal PosXdif2 = GetPosXdif(PositionX.add(PosXdif1.multiply(h).multiply(new BigDecimal("0.5"))),
				PositionY.add(PosYdif1.multiply(h).multiply(new BigDecimal("0.5"))),
				SpeedX.add(VelXdif1.multiply(h).multiply(new BigDecimal("0.5"))),
				SpeedY.add(VelYdif1.multiply(h).multiply(new BigDecimal("0.5"))));
		BigDecimal PosYdif2 = GetPosYdif(PositionX.add(PosXdif1.multiply(h).multiply(new BigDecimal("0.5"))),
				PositionY.add(PosYdif1.multiply(h).multiply(new BigDecimal("0.5"))),
				SpeedX.add(VelXdif1.multiply(h).multiply(new BigDecimal("0.5"))),
				SpeedY.add(VelYdif1.multiply(h).multiply(new BigDecimal("0.5"))));
		BigDecimal VelXdif2 = GetVelXdif(PositionX.add(PosXdif1.multiply(h).multiply(new BigDecimal("0.5"))),
				PositionY.add(PosYdif1.multiply(h).multiply(new BigDecimal("0.5"))),
				SpeedX.add(VelXdif1.multiply(h).multiply(new BigDecimal("0.5"))),
				SpeedY.add(VelYdif1.multiply(h).multiply(new BigDecimal("0.5"))));
		BigDecimal VelYdif2 = GetVelYdif(PositionX.add(PosXdif1.multiply(h).multiply(new BigDecimal("0.5"))),
				PositionY.add(PosYdif1.multiply(h).multiply(new BigDecimal("0.5"))),
				SpeedX.add(VelXdif1.multiply(h).multiply(new BigDecimal("0.5"))),
				SpeedY.add(VelYdif1.multiply(h).multiply(new BigDecimal("0.5"))));

		BigDecimal PosXdif3 = GetPosXdif(PositionX.add(PosXdif2.multiply(h).multiply(new BigDecimal("0.5"))),
				PositionY.add(PosYdif2.multiply(h).multiply(new BigDecimal("0.5"))),
				SpeedX.add(VelXdif2.multiply(h).multiply(new BigDecimal("0.5"))),
				SpeedY.add(VelYdif2.multiply(h).multiply(new BigDecimal("0.5"))));
		BigDecimal PosYdif3 = GetPosYdif(PositionX.add(PosXdif2.multiply(h).multiply(new BigDecimal("0.5"))),
				PositionY.add(PosYdif2.multiply(h).multiply(new BigDecimal("0.5"))),
				SpeedX.add(VelXdif2.multiply(h).multiply(new BigDecimal("0.5"))),
				SpeedY.add(VelYdif2.multiply(h).multiply(new BigDecimal("0.5"))));
		BigDecimal VelXdif3 = GetVelXdif(PositionX.add(PosXdif2.multiply(h).multiply(new BigDecimal("0.5"))),
				PositionY.add(PosYdif2.multiply(h).multiply(new BigDecimal("0.5"))),
				SpeedX.add(VelXdif2.multiply(h).multiply(new BigDecimal("0.5"))),
				SpeedY.add(VelYdif2.multiply(h).multiply(new BigDecimal("0.5"))));
		BigDecimal VelYdif3 = GetVelYdif(PositionX.add(PosXdif2.multiply(h).multiply(new BigDecimal("0.5"))),
				PositionY.add(PosYdif2.multiply(h).multiply(new BigDecimal("0.5"))),
				SpeedX.add(VelXdif2.multiply(h).multiply(new BigDecimal("0.5"))),
				SpeedY.add(VelYdif2.multiply(h).multiply(new BigDecimal("0.5"))));

		BigDecimal PosXdif4 = GetPosXdif(PositionX.add(PosXdif3.multiply(h)), PositionY.add(PosYdif3.multiply(h)),
				SpeedX.add(VelXdif3.multiply(h)), SpeedY.add(VelYdif3.multiply(h)));
		BigDecimal PosYdif4 = GetPosYdif(PositionX.add(PosXdif3.multiply(h)), PositionY.add(PosYdif3.multiply(h)),
				SpeedX.add(VelXdif3.multiply(h)), SpeedY.add(VelYdif3.multiply(h)));
		BigDecimal VelXdif4 = GetVelXdif(PositionX.add(PosXdif3.multiply(h)), PositionY.add(PosYdif3.multiply(h)),
				SpeedX.add(VelXdif3.multiply(h)), SpeedY.add(VelYdif3.multiply(h)));
		BigDecimal VelYdif4 = GetVelYdif(PositionX.add(PosXdif3.multiply(h)), PositionY.add(PosYdif3.multiply(h)),
				SpeedX.add(VelXdif3.multiply(h)), SpeedY.add(VelYdif3.multiply(h)));

		BigDecimal PosXdif = new BigDecimal(PosXdif1.toString());
		PosXdif=PosXdif.add(PosXdif2.multiply(new BigDecimal(2)));
		PosXdif=PosXdif.add(PosXdif3.multiply(new BigDecimal(2)));
		PosXdif=PosXdif.add(PosXdif4);
		PosXdif=PosXdif.multiply(os);
		PositionX=PositionX.add(PosXdif.multiply(h));
		PositionX=PositionX.setScale(Accu, RoundingMode.FLOOR);

		BigDecimal PosYdif = new BigDecimal(PosYdif1.toString());
		PosYdif=PosYdif.add(PosYdif2.multiply(new BigDecimal(2)));
		PosYdif=PosYdif.add(PosYdif3.multiply(new BigDecimal(2)));
		PosYdif=PosYdif.add(PosYdif4);
		PosYdif=PosYdif.multiply(os);
		PositionY=PositionY.add(PosYdif.multiply(h));
		PositionY=PositionY.setScale(Accu, RoundingMode.FLOOR);

		BigDecimal VelXdif = new BigDecimal(VelXdif1.toString());
		VelXdif=VelXdif.add(VelXdif2.multiply(new BigDecimal(2)));
		VelXdif=VelXdif.add(VelXdif3.multiply(new BigDecimal(2)));
		VelXdif=VelXdif.add(VelXdif4);
		VelXdif=VelXdif.multiply(os);
		SpeedX=SpeedX.add(VelXdif.multiply(h));
		SpeedX=SpeedX.setScale(Accu, RoundingMode.FLOOR);

		BigDecimal VelYdif = new BigDecimal(VelYdif1.toString());
		VelYdif=VelYdif.add(VelYdif2.multiply(new BigDecimal(2)));
		VelYdif=VelYdif.add(VelYdif3.multiply(new BigDecimal(2)));
		VelYdif=VelYdif.add(VelYdif4);
		VelYdif=VelYdif.multiply(os);
		SpeedY=SpeedY.add(VelYdif.multiply(h));
		SpeedY=SpeedY.setScale(Accu, RoundingMode.FLOOR);

		return new Vector(PositionX, PositionY);
	}

	BigDecimal GetPosXdif(BigDecimal posx, BigDecimal posy, BigDecimal velx, BigDecimal vely) {
		return velx.multiply(new BigDecimal("0.000001"));
	}

	BigDecimal GetPosYdif(BigDecimal posx, BigDecimal posy, BigDecimal velx, BigDecimal vely) {
		return vely.multiply(new BigDecimal("0.000001"));
	}

	BigDecimal GetVelXdif(BigDecimal posx, BigDecimal posy, BigDecimal velx, BigDecimal vely) {
		return GetAccel(new Vector(posx, posy)).X;
	}

	BigDecimal GetVelYdif(BigDecimal posx, BigDecimal posy, BigDecimal velx, BigDecimal vely) {
		return GetAccel(new Vector(posx, posy)).Y;
	}

	private Vector GetAccel(Vector pos) {
		BigDecimal r = pos.X.multiply(pos.X);
		r = r.add(pos.Y.multiply(pos.Y));
		r = sqrt(r, Accu);
		BigDecimal a = Mass.multiply(new BigDecimal("0.667408313131313131313131313131313131313131313131313131").setScale(Accu, RoundingMode.FLOOR));
		a = a.divide(r, RoundingMode.FLOOR);
		a = a.divide(r, RoundingMode.FLOOR);
		Vector va = new Vector(pos.X, pos.Y);
		va.Multiply(a.divide(r, RoundingMode.FLOOR));
		va.Multiply(new BigDecimal(-1));
		return va;// m/s^2
	}

	public static BigDecimal sqrt(BigDecimal in, int scale) {
		BigDecimal sqrt = new BigDecimal(1);
		sqrt.setScale(scale + 3, RoundingMode.FLOOR);
		BigDecimal store = new BigDecimal(in.toString());
		boolean first = true;
		do {
			if (!first) {
				store = new BigDecimal(sqrt.toString());
			} else
				first = false;
			store.setScale(scale + 3, RoundingMode.FLOOR);
			sqrt = in.divide(store, scale + 3, RoundingMode.FLOOR).add(store).divide(BigDecimal.valueOf(2), scale + 3,
					RoundingMode.FLOOR);
		} while (!store.equals(sqrt));
		return sqrt.setScale(scale, RoundingMode.FLOOR);
	}
}
