package org.narses.narsion.dev.math.geometry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;

public class GeoPolygon {

	// Polygon face planes
	private List<double[]> facePlanes;

	// Polygon faces
	private List<Pos[]> faces;

	// Number of faces
	private int numberOfFaces;

	public static GeoPolygon of(Pos... points) {
		return new GeoPolygon(points);
	}
	
	public static GeoPolygon of(Collection<Pos> list) {
		return of(list.toArray(Pos[]::new));
	}

	private GeoPolygon(Pos[] polygonInst) {

		// Set boundary
		this.set3DPolygonBoundary(polygonInst);

		// Set faces and face planes
		this.setConvex3DFaces(polygonInst);
	}

	public List<double[]> getFacePlanes() {
		return this.facePlanes;
	}

	public List<Pos[]> getFaces() {
		return this.faces;
	}

	public boolean isPointInside3DPolygon(Point point) {

		for (int i = 0; i < this.numberOfFaces; i++) {

			double[] pl = this.facePlanes.get(i);
			final double dis = ((point.x() * pl[0]) + (point.y() * pl[1]) + (point.z() * pl[2]) + pl[3]);

			// If the point is in the same half space with normal vector for any face of the
			// cube,
			// then it is outside of the 3D polygon
			if (dis > 0)
				return false;
		}

		// If the point is in the opposite half space with normal vector for all 6
		// faces,
		// then it is inside of the 3D polygon
		return true;
	}

	private void set3DPolygonBoundary(Pos[] polygon) {

		final int n = polygon.length;

		double xmin, xmax, ymin, ymax, zmin, zmax;

		xmin = xmax = polygon[0].x();
		ymin = ymax = polygon[0].y();
		zmin = zmax = polygon[0].z();

		for (int i = 1; i < n; i++) {
			if (polygon[i].x() < xmin) {
				xmin = polygon[i].x();
			}
			if (polygon[i].y() < ymin) {
				ymin = polygon[i].y();
			}
			if (polygon[i].z() < zmin) {
				zmin = polygon[i].z();
			}
			if (polygon[i].x() > xmax) {
				xmax = polygon[i].x();
			}
			if (polygon[i].y() > ymax) {
				ymax = polygon[i].y();
			}
			if (polygon[i].z() > zmax) {
				zmax = polygon[i].z();
			}
		}
	}

	private void setConvex3DFaces(Pos[] polygon) {
		final ArrayList<Pos[]> faces = new ArrayList<>();

		final ArrayList<double[]> facePlanes = new ArrayList<>();

		int numberOfFaces;

		// Maximum point to face plane distance error,
		// point is considered in the face plane if its distance is less than this error
		final double maxError = 0.1;

		// vertices of 3D polygon
		final int n = polygon.length;

		// vertices indexes for all faces
		// vertices index is the original index value in the input polygon
		final ArrayList<ArrayList<Integer>> faceVerticeIndex = new ArrayList<>();

		// face planes for all faces
		final ArrayList<double[]> fpOutward = new ArrayList<>();

		for (int i = 0; i < n; i++) {
			// triangle point 1
			final Pos p0 = polygon[i];

			for (int j = i + 1; j < n; j++) {
				// triangle point 2
				final Pos p1 = polygon[j];

				for (int k = j + 1; k < n; k++) {
					// triangle point 3
					final Pos p2 = polygon[k];

					// Plane Equation: a * x + b * y + c * z + d = 0
					// Convert triple position to plane equation
					final Point v = p0.sub(p1);

					final Point u = p0.sub(p2);

					final double x = (u.y() * v.z()) - (u.z() * v.y());
					final double y = (u.z() * v.x()) - (u.x() * v.z());
					final double z = (u.x() * v.y()) - (u.y() * v.x());

					final Pos p11 = p0.add(x, y, z);

					final Point n1 = p0.sub(p11);

					// normal vector
					final double a = n1.x();
					final double b = n1.y();
					final double c = n1.z();
					final double d = -((a * p0.x()) + (b * p0.y()) + (c * p0.z()));

					final double[] trianglePlane = new double[]{a, b, c, d};

					int onLeftCount = 0;
					int onRightCount = 0;

					// indexes of points that lie in same plane with face triangle plane
					final ArrayList<Integer> pointInSamePlaneIndex = new ArrayList<>();

					for (int l = 0; l < n; l++) {
						// any point other than the 3 triangle points
						if ((l != i) && (l != j) && (l != k)) {
							final Pos p = polygon[l];

							final double dis = ((((Point) p).x() * trianglePlane[0]) + (((Point) p).y() * trianglePlane[1]) + (((Point) p).z() * trianglePlane[2]) + trianglePlane[3]);

							// next point is in the triangle plane
							if (Math.abs(dis) < maxError) {
								pointInSamePlaneIndex.add(l);
							} else {
								if (dis < 0) {
									onLeftCount++;
								} else {
									onRightCount++;
								}
							}
						}
					}

					// This is a face for a CONVEX 3d polygon.
					// For a CONCAVE 3d polygon, this maybe not a face.
					if ((onLeftCount == 0) || (onRightCount == 0)) {
						final ArrayList<Integer> verticeIndexInOneFace = new ArrayList<>();

						// triangle plane
						verticeIndexInOneFace.add(i);
						verticeIndexInOneFace.add(j);
						verticeIndexInOneFace.add(k);

						final int m = pointInSamePlaneIndex.size();

						if (m > 0) // there are other vertices in this triangle plane
						{
							verticeIndexInOneFace.addAll(pointInSamePlaneIndex);
						}

						// if verticeIndexInOneFace is a new face,
						// add it in the faceVerticeIndex list,
						// add the trianglePlane in the face plane list fpOutward
						if (!faceVerticeIndex.contains(verticeIndexInOneFace)) {
							faceVerticeIndex.add(verticeIndexInOneFace);

							if (onRightCount == 0) {
								fpOutward.add(trianglePlane);
							} else {
								fpOutward.add(new double[]{-trianglePlane[0], -trianglePlane[1], -trianglePlane[2], -trianglePlane[3]});
							}
						}
					} else {
						// possible reasons:
						// 1. the plane is not a face of a convex 3d polygon,
						// it is a plane crossing the convex 3d polygon.
						// 2. the plane is a face of a concave 3d polygon
					}

				} // k loop
			} // j loop
		} // i loop

		// return number of faces
		numberOfFaces = faceVerticeIndex.size();

		for (int i = 0; i < numberOfFaces; i++) {
			// return face planes
			facePlanes.add(fpOutward.get(i));

			final List<Integer> vi = new ArrayList<>();

			final int count = faceVerticeIndex.get(i).size();
			final Pos[] gp = new Pos[count];

			List<Integer> face = faceVerticeIndex.get(i);

			for (int j = 0; j < count; j++) {
				vi.add(face.get(j));
				gp[j] = polygon[vi.get(j)];
			}

			// return faces
			faces.add(gp);
		}

		this.faces = faces;
		this.facePlanes = facePlanes;
		this.numberOfFaces = numberOfFaces;
	}
}