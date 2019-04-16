package ch.coredump.twobutton.util;

public class Colors {
	public static class Col {

		public Col(float red, float green, float blue, float alpha) {
			this.r = red;
			this.g = green;
			this.b = blue;
			this.a = alpha;
		}

		public Col(float col) {
			this(col, col, col, col);
		}

		public float r = 0;
		public float g = 0;
		public float b = 0;
		public float a = 0;
	}

	public static float background() {
		return 0;
	}

	public static float floor() {
		return 155;
	}

	public static Col overlay() {
		return new Col(255);
	}

	public static Col stars() {
		return new Col(255, 255, 255, 100);
	}

	public static Col obstacle() {
		return new Col(255, 255, 0, 200);
	}
}
