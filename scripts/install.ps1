#Requires -Version 5.1
<#
.SYNOPSIS
    Download the latest S&S Companion native image for this machine and unblock it.
.PARAMETER Directory
    Where to place companion.exe. Defaults to the current directory.
#>
[CmdletBinding()]
param([string]$Directory = $PWD)

$ErrorActionPreference = 'Stop'
[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12

$repo = 'spies-and-spiders/companion'

switch ($env:PROCESSOR_ARCHITECTURE) {
    'AMD64' { $arch = 'x64' }
    'ARM64' {
        $arch = 'x64'
        Write-Warning 'No native ARM64 build; installing the x64 binary, which runs under emulation.'
    }
    default { throw "Unsupported architecture: $env:PROCESSOR_ARCHITECTURE" }
}

if (-not (Test-Path -LiteralPath $Directory -PathType Container)) {
    throw "No such directory: $Directory"
}
$dest = Join-Path (Resolve-Path -LiteralPath $Directory) 'companion.exe'

$release = Invoke-RestMethod "https://api.github.com/repos/$repo/releases/latest"
$asset = $release.assets |
    Where-Object { $_.name -like "companion-*-windows-$arch.exe.gz" } |
    Select-Object -First 1
if (-not $asset) { throw "No windows-$arch asset in release $($release.tag_name)." }

$tmp = Join-Path ([IO.Path]::GetTempPath()) ([IO.Path]::GetRandomFileName())
try {
    Write-Host "Downloading $($release.tag_name) (windows-$arch)..."
    $ProgressPreference = 'SilentlyContinue'
    Invoke-WebRequest -Uri $asset.browser_download_url -OutFile $tmp

    $input = [IO.File]::OpenRead($tmp)
    $gzip = New-Object IO.Compression.GZipStream($input, [IO.Compression.CompressionMode]::Decompress)
    $output = [IO.File]::Create($dest)
    try { $gzip.CopyTo($output) }
    finally { $output.Dispose(); $gzip.Dispose(); $input.Dispose() }
}
finally {
    Remove-Item -LiteralPath $tmp -Force -ErrorAction SilentlyContinue
}

# Clears the mark-of-the-web a browser download would have attached.
Unblock-File -LiteralPath $dest

Write-Host "Installed $($release.tag_name) to $dest"
