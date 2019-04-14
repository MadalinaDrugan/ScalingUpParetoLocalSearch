/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package general;

/**
 *
 * @author madalina
 */
public interface PerformanceInterface {
    public boolean performanceImprovement(ArchiveSolutions p, ArchiveSolutions children, ArchiveSolutions nda);
    public boolean performanceImprovement(Solution p, Solution children);
}
