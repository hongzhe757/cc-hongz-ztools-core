package cc.hongz.ztools.core.net;

import cc.hongz.ztools.core.exceptions.UtilException;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.LinkedHashSet;

/**
 * 网络相关工具（最小化版本）.
 * <p>
 * 仅迁移 {@code getLocalHardwareAddress()} 及相关方法供 IdUtil 使用。
 * <p>
 * 迁移自 hutool {@code cn.hutool.core.net.NetUtil}。
 *
 * @author looly
 */
public class NetUtil {

    private NetUtil() {
    }

    /**
     * 获得本机物理地址.
     *
     * @return 本机MAC地址
     */
    public static byte[] getLocalHardwareAddress() {
        return getHardwareAddress(getLocalhost());
    }

    /**
     * 获得指定地址信息中的硬件地址.
     *
     * @param inetAddress {@link InetAddress}
     * @return 硬件地址
     */
    public static byte[] getHardwareAddress(final InetAddress inetAddress) {
        if (null == inetAddress) {
            return null;
        }

        try {
            final NetworkInterface networkInterface = NetworkInterface.getByInetAddress(inetAddress);
            if (null != networkInterface) {
                return networkInterface.getHardwareAddress();
            }
        } catch (final SocketException e) {
            throw new UtilException(e);
        }
        return null;
    }

    /**
     * 获取本机网卡IP地址，规则如下：
     * <pre>
     * 1. 查找所有网卡地址，必须非回路（loopback）地址、非局域网地址（siteLocal）、IPv4地址
     * 2. 如果无满足要求的地址，调用 {@link InetAddress#getLocalHost()} 获取地址
     * </pre>
     *
     * @return 本机网卡IP地址，获取失败返回 {@code null}
     */
    public static InetAddress getLocalhost() {
        final LinkedHashSet<InetAddress> localAddressList = localAddressList(address -> {
            return !address.isLoopbackAddress() && address instanceof Inet4Address;
        });

        if (!localAddressList.isEmpty()) {
            InetAddress address2 = null;
            for (final InetAddress inetAddress : localAddressList) {
                if (!inetAddress.isSiteLocalAddress()) {
                    return inetAddress;
                } else if (null == address2) {
                    address2 = inetAddress;
                }
            }
            if (null != address2) {
                return address2;
            }
        }

        try {
            return InetAddress.getLocalHost();
        } catch (final UnknownHostException e) {
            // ignore
        }
        return null;
    }

    /**
     * 获取所有满足过滤条件的本地IP地址对象.
     *
     * @param addressFilter 过滤器，null表示不过滤
     * @return 过滤后的地址对象列表
     */
    public static LinkedHashSet<InetAddress> localAddressList(final java.util.function.Predicate<InetAddress> addressFilter) {
        Enumeration<NetworkInterface> networkInterfaces;
        try {
            networkInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (final SocketException e) {
            throw new UtilException(e);
        }

        if (networkInterfaces == null) {
            throw new UtilException("Get network interface error!");
        }

        final LinkedHashSet<InetAddress> ipSet = new LinkedHashSet<>();
        while (networkInterfaces.hasMoreElements()) {
            final NetworkInterface networkInterface = networkInterfaces.nextElement();
            final Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                final InetAddress inetAddress = inetAddresses.nextElement();
                if (inetAddress != null && (null == addressFilter || addressFilter.test(inetAddress))) {
                    ipSet.add(inetAddress);
                }
            }
        }
        return ipSet;
    }
}
