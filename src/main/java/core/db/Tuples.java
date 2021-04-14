package core.db;

public class Tuples
{
	public static <T1, T2> Tuple2<T1, T2> tuple2(T1 t1, T2 t2)
	{
		return new Tuples.Tuple2<T1, T2>(t1, t2);
	}

	public static <T1, T2, T3> Tuple3<T1, T2, T3> tuple3(T1 t1, T2 t2, T3 t3)
	{
		return new Tuples.Tuple3<T1, T2, T3>(t1, t2, t3);
	}

	public static class Tuple2<T1, T2>
	{
		protected T1 t1;
		protected T2 t2;

		public Tuple2(T1 f1, T2 f2)
		{
			this.t1 = f1;
			this.t2 = f2;
		}

		public T1 getT1()
		{
			return t1;
		}

		public T2 getT2()
		{
			return t2;
		}

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + ((t1 == null) ? 0 : t1.hashCode());
			result = prime * result + ((t2 == null) ? 0 : t2.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Tuple2 other = (Tuple2) obj;
			if (t1 == null)
			{
				if (other.t1 != null)
					return false;
			}
			else if (!t1.equals(other.t1))
				return false;
			if (t2 == null)
			{
				if (other.t2 != null)
					return false;
			}
			else if (!t2.equals(other.t2))
				return false;
			return true;
		}

		@Override
		public String toString()
		{
			return "Tuple2 [t1=" + t1 + ", t2=" + t2 + "]";
		}

	}

	public static class Tuple3<T1, T2, T3> extends Tuple2
	{
		protected T3 t3;

		public Tuple3(T1 f1, T2 f2, T3 f3)
		{
			super(f1, f2);
			this.t3 = f3;
		}

		public T3 getT3()
		{
			return t3;
		}

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + ((t3 == null) ? 0 : t3.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			Tuple3 other = (Tuple3) obj;
			if (t3 == null)
			{
				if (other.t3 != null)
					return false;
			}
			else if (!t3.equals(other.t3))
				return false;
			return true;
		}

		@Override
		public String toString()
		{
			return "Tuple3 [t1=" + t1 + ", t2=" + t2 + ", t3=" + t3 + "]";
		}		
	}

	public static class Tuple4<T1, T2, T3, T4> extends Tuple3
	{
		protected T4 t4;

		public Tuple4(T1 f1, T2 f2, T3 f3, T4 f4)
		{
			super(f1, f2, f3);
			this.t4 = f4;
		}

		public T4 getT4()
		{
			return t4;
		}

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + ((t4 == null) ? 0 : t4.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			Tuple4 other = (Tuple4) obj;
			if (t4 == null)
			{
				if (other.t4 != null)
					return false;
			}
			else if (!t4.equals(other.t4))
				return false;
			return true;
		}

		@Override
		public String toString()
		{
			return "Tuple3 [t1=" + t1 + ", t2=" + t2 + ", t3=" + t3 + ", t4=" + t4 + "]";
		}

	}

}
