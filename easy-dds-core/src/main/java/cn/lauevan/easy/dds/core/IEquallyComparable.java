package cn.lauevan.easy.dds.core;

/**
 * Where the interface is inherited or implemented, equality guarantees can be made:
 * equality as long as the object content or object address is the same.
 *
 * This interface requires that whenever the interface is inherited or implemented,
 * the {@link Object#equals(Object)} and {@link Object#hashCode()} methods should be overwritten to ensure that:
 *
 * <p>
 *     <ul>
 *         <li>When doing a {@code equals} comparison, two objects of the same content are equal.</li>
 *         <li>Two equal objects, whose {@code hashCode} are also the same, conform to the semantics.</li>
 *         <li>{@code key} can be used as the {@code key} of {@link java.util.HashMap}.</li>
 *     </ul>
 * </p>
 *
 * @see Object#equals(Object)
 * @see Object#hashCode()
 *
 * @author Lauevan (noah.coder@gmail.com)
 * Create at November 19, 2020 at 09:57:10 GMT+8
 */
public interface IEquallyComparable {
}
