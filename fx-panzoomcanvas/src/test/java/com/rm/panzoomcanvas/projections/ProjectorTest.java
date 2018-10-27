/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rm.panzoomcanvas.projections;

import com.rm.panzoomcanvas.core.Level;
import com.rm.panzoomcanvas.core.ScreenEnvelope;
import com.rm.panzoomcanvas.core.VirtualPoint;
import com.rm.panzoomcanvas.core.VirtualEnvelope;
import com.rm.panzoomcanvas.core.ScreenPoint;
import com.rm.panzoomcanvas.core.FxEnvelope;
import com.rm.panzoomcanvas.core.FxPoint;
import com.rm.panzoomcanvas.core.Point;
import com.rm.panzoomcanvas.core.SpatialRef;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;

/**
 *
 * @author rmarquez
 */
@RunWith(JUnitParamsRunner.class)
public class ProjectorTest {
  
  public ProjectorTest() {
  }
  
  @Parameters({"0.5, 0.5"})
  @Test
  public void testProjectScreenToVirtualStrict(
          double screenCnterX, double screenCenterY) {
    System.out.println("projectScreenToVirtualStrict");
    ScreenPoint screenMin = new ScreenPoint(0, 0);
    ScreenPoint screenMax = new ScreenPoint(120, 120);
    
    Level level = new Level(0, null);
    ScreenPoint center = new ScreenPoint(60, 60);
    ScreenEnvelope screenEnvVal = new ScreenEnvelope(screenMin, screenMax, level, center);
    Projector instance = new Projector(new MapCanvasSR(), (FxPoint geomPoint, SpatialRef baseSpatialRef) -> geomPoint);
    VirtualEnvelope result = instance.projectScreenToVirtual(screenEnvVal);
    
    VirtualEnvelope expResult = new VirtualEnvelope(new VirtualPoint(-60, -60), new VirtualPoint(60, 60));
    System.out.println(expResult);
    System.out.println(result);
    double tol = 1e-5;
    Assert.assertEquals("max x equals", expResult.getMax().getX(), result.getMax().getX(), tol);
    Assert.assertEquals("max y equals", expResult.getMax().getY(), result.getMax().getY(), tol);
    Assert.assertEquals("min x equals", expResult.getMin().getX(), result.getMin().getX(), tol);
    Assert.assertEquals("min y equals", expResult.getMin().getX(), result.getMin().getY(), tol);
  }
}
