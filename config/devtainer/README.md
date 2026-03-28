Add this to your `~/.ssh/config`:

```
Host aquadx-devtainer
  HostName localhost
  Port 2322
  User root
  StrictHostKeyChecking no
```

Then you can use `ssh aquadx-devtainer` to connect to the container.
